package com.tfxsoftware.memserver.modules.events;

import com.tfxsoftware.memserver.modules.events.dto.CreateEventDto;
import com.tfxsoftware.memserver.modules.rosters.Roster;
import com.tfxsoftware.memserver.modules.rosters.RosterRepository;
import com.tfxsoftware.memserver.modules.users.User;
import com.tfxsoftware.memserver.modules.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRegistrationRepository eventRegistrationRepository;
    @Mock
    private RosterRepository rosterRepository;

    @InjectMocks
    private EventService eventService;

    private User testUser;
    private Roster testRoster;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .balance(BigDecimal.valueOf(1000))
                .build();

        testRoster = Roster.builder()
                .id(UUID.randomUUID())
                .owner(testUser)
                .name("Test Roster")
                .region(User.Region.NA)
                .activity(Roster.RosterActivity.IDLE)
                .build();

        testEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Test Event")
                .regions(Set.of(User.Region.NA))
                .status(Event.EventStatus.OPEN)
                .entryFee(BigDecimal.valueOf(100))
                .maxPlayers(1) // Set max players for the test
                .build();
    }

    @Test
    void testCreateEventSuccess() {
        // Mock repository behavior
        when(eventRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        CreateEventDto dto = CreateEventDto.builder()
                .name("New Event")
                .regions(Set.of(User.Region.NA))
                .type(Event.EventType.LEAGUE)
                .tier(Event.Tier.B)
                .entryFee(BigDecimal.ZERO)
                .totalPrizePool(BigDecimal.ZERO)
                .opensAt(LocalDateTime.now().minusDays(1))
                .startsAt(LocalDateTime.now().plusDays(1))
                .gamesPerBlock(5)
                .minutesBetweenGames(10)
                .minutesBetweenBlocks(30)
                .maxPlayers(5) // New field
                .roundRobinCount(1)
                .build();

        Event createdEvent = eventService.createEvent(dto);

        assertNotNull(createdEvent);
        assertEquals(dto.getName(), createdEvent.getName());
        assertEquals(dto.getMaxPlayers(), createdEvent.getMaxPlayers()); // Assert maxPlayers
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testRegisterForEventWhenEventIsFull() {
        // Setup a full event
        testEvent.setRegistrations(new ArrayList<>(Collections.nCopies(testEvent.getMaxPlayers(), EventRegistration.builder().roster(testRoster).build())));

        when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));
        when(rosterRepository.findById(testRoster.getId())).thenReturn(Optional.of(testRoster));
        when(eventRegistrationRepository.findByRosterIdAndEventId(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                eventService.registerForEvent(testEvent.getId(), testRoster.getId(), testUser));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Event is full. Maximum players reached.", exception.getReason());
    }

    // You might want to add more comprehensive tests for other scenarios like
    // successful registration, insufficient balance, wrong region, etc.
}