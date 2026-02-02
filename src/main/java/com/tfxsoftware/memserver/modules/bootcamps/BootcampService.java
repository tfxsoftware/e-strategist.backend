package com.tfxsoftware.memserver.modules.bootcamps;

import com.tfxsoftware.memserver.modules.bootcamps.dto.CreateBootcampSessionDto;
import com.tfxsoftware.memserver.modules.rosters.Roster;
import com.tfxsoftware.memserver.modules.rosters.RosterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BootcampService {

    private final RosterRepository rosterRepository;
    private final BootcampSessionRepository sessionRepository;

    //private static final long BASE_XP = 1000L;
    private static final int TICK_HOURS = 6;

    /**
     * Starts the bootcamp and creates the transient configuration.
     */
    @Transactional
    public void startBootcamp(UUID rosterId, CreateBootcampSessionDto request) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster not found"));

        if (roster.getActivity() != Roster.RosterActivity.IDLE) {
            throw new IllegalStateException("Roster must be IDLE to start bootcamp");
        }

        LocalDateTime now = LocalDateTime.now();
        
        // 1. Create the Session
        BootcampSession session = BootcampSession.builder()
                .roster(roster)
                .startedAt(now)
                .lastTickAt(now)
                .build();

        // 2. Map DTOs to Entities using the wrapper list
        List<PlayerTrainingConfig> entities = request.configs().stream().map(dto -> 
            PlayerTrainingConfig.builder()
                .session(session)
                .playerId(dto.playerId())
                .targetRole(dto.targetRole())
                .primaryHeroId(dto.primaryHeroId())
                .secondaryHeroId1(dto.secondaryHeroId1())
                .secondaryHeroId2(dto.secondaryHeroId2())
                .build()
        ).toList();

        session.setPlayerConfigs(entities);
        
        // 3. Update Roster Status
        roster.setActivity(Roster.RosterActivity.BOOTCAMP);
        
        sessionRepository.save(session);
        rosterRepository.save(roster);
        
        log.info("Bootcamp session created for roster {}", rosterId);
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    @Transactional
    public void processBootcampTicks() {
        List<BootcampSession> activeSessions = sessionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (BootcampSession session : activeSessions) {
            if (session.getLastTickAt().plusHours(TICK_HOURS).isBefore(now)) {
                applyXpTick(session);
                session.setLastTickAt(now);
                sessionRepository.save(session);
            }
        }
    }

    private void applyXpTick(BootcampSession session) {
        
    }

    @Transactional
    public void stopBootcamp(UUID rosterId) {
        Roster roster = rosterRepository.findById(rosterId).orElseThrow();
        sessionRepository.deleteById(rosterId); // Cascades to configs
        roster.setActivity(Roster.RosterActivity.IDLE);
        rosterRepository.save(roster);
    }
}