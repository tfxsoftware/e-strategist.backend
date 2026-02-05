package com.tfxsoftware.memserver.modules.events;

import java.util.UUID;

import com.tfxsoftware.memserver.modules.events.dto.CreateEventDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfxsoftware.memserver.modules.users.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody CreateEventDto createEventDto) {
        Event newEvent = eventService.createEvent(createEventDto);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    @PostMapping("/{eventId}/register/roster/{rosterId}")
    public ResponseEntity<EventRegistration> registerForEvent(
            @PathVariable UUID eventId,
            @PathVariable UUID rosterId,
            @AuthenticationPrincipal User currentUser) {
        
        EventRegistration registration = eventService.registerForEvent(eventId, rosterId, currentUser);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }
}
