package com.tfxsoftware.memserver.modules.rosters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnergyService {

    private final RosterRepository rosterRepository;

    private static final int MAX_ENERGY = 100;
    private static final int IDLE_RECOVERY = 2;
    private static final int EVENT_RECOVERY = 1;

    @Scheduled(cron = "0 0 * * * *") // Every hour
    @Transactional
    public void processEnergyTicks() {
        log.info("Processing hourly energy recovery ticks");
        List<Roster> rosters = rosterRepository.findAllByEnergyLessThan(MAX_ENERGY);

        for (Roster roster : rosters) {
            int currentEnergy = roster.getEnergy() != null ? roster.getEnergy() : 0;

            int recovery = 0;
            if (roster.getActivity() == Roster.RosterActivity.IDLE) {
                recovery = IDLE_RECOVERY;
            } else if (roster.getActivity() == Roster.RosterActivity.IN_EVENT) {
                recovery = EVENT_RECOVERY;
            }

            if (recovery > 0) {
                roster.setEnergy(Math.min(MAX_ENERGY, currentEnergy + recovery));
                log.info("Recovered {} energy for roster {} (Activity: {}). New energy: {}", 
                        recovery, roster.getName(), roster.getActivity(), roster.getEnergy());
            }
        }
        rosterRepository.saveAll(rosters);
    }
}
