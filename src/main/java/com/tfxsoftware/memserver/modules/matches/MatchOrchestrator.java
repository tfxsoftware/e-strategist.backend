package com.tfxsoftware.memserver.modules.matches;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The MatchOrchestrator is the background engine that triggers simulations.
 * It periodically scans the database for scheduled matches that have reached their start time.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchOrchestrator {

    private final MatchRepository matchRepository;
    private final MatchEngineService matchEngineService;

    /**
     * Runs every minute (at the 30-second mark to offset from EventOrchestrator).
     * This processes any match whose scheduled time has arrived.
     */
    @Scheduled(cron = "30 * * * * *")
    public void runScheduledMatches() {
        LocalDateTime now = LocalDateTime.now();
        
        List<Match> pendingMatches = matchRepository.findAllByStatusAndScheduledTimeBefore(
                Match.MatchStatus.SCHEDULED, 
                now
        );

        if (pendingMatches.isEmpty()) {
            return;
        }

        log.info("MatchOrchestrator found {} matches ready for simulation.", pendingMatches.size());

        for (Match match : pendingMatches) {
            try {
                // We process each match individually.
                // The @Transactional inside simulateMatch ensures each game is its own atomic unit.
                matchEngineService.simulateMatch(match.getId());
            } catch (Exception e) {
                log.error("Critical failure simulating match {}: {}", match.getId(), e.getMessage());
                // We don't throw here so one broken match doesn't stop the whole batch
            }
        }
    }
}