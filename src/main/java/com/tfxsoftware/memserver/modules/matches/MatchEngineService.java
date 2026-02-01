package com.tfxsoftware.memserver.modules.matches;

import com.tfxsoftware.memserver.modules.heroes.Hero;
import com.tfxsoftware.memserver.modules.heroes.HeroService;
import com.tfxsoftware.memserver.modules.players.PlayerService;
import com.tfxsoftware.memserver.modules.rosters.RosterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchEngineService {

    private final MatchRepository matchRepository;
    private final PlayerService playerService;
    private final HeroService heroService;
    private final RosterService rosterService;
    private final MatchResultService matchResultService;

    /**
     * Step 1: Entry point for the simulation.
     */
    @Transactional
    public void simulateMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        if (match.getStatus() != Match.MatchStatus.SCHEDULED) return;

        log.info("Starting simulation for Match: {}", matchId);

        // Step 2: Resolve Draft (Intentions vs Bans vs Availability)
        Map<UUID, Hero> finalizedPicks = resolveDraft(match);

        // TODO: Next steps (Power Calculation, Determination of Winner, Persistence)
        log.info("Draft resolved. Final picks determined for all 10 players.");
    }

    /**
     * Implements the Asynchronous Draft Logic:
     * 1. Unified Ban Set.
     * 2. Sequential picking based on pickOrder.
     * 3. Emergency Meta Picks if preferences are unavailable.
     */
    private Map<UUID, Hero> resolveDraft(Match match) {
        // 1. Create the Global Ban Set (No hero in this set can be picked)
        Set<UUID> unavailableHeroes = Stream.concat(
                match.getHomeBans().stream(),
                match.getAwayBans().stream()
        ).collect(Collectors.toSet());

        // 2. Prepare the ordered pick sequence (1 to 10)
        // We merge home and away picks and sort by the pickOrder field
        List<DraftEntry> draftSequence = Stream.concat(
                match.getHomePickIntentions().stream().map(p -> new DraftEntry(p, true)),
                match.getAwayPickIntentions().stream().map(p -> new DraftEntry(p, false))
        ).sorted(Comparator.comparingInt(e -> e.pick().getPickOrder())).toList();

        Map<UUID, Hero> finalPicks = new HashMap<>();
        List<Hero> allHeroes = heroService.findAll();

        for (DraftEntry entry : draftSequence) {
            Match.MatchPick intent = entry.pick();
            Hero assignedHero = null;

            // Try Preference 1
            assignedHero = tryAssign(intent.getPreferredHeroId1(), unavailableHeroes, allHeroes);

            // Try Preference 2 if 1 failed
            if (assignedHero == null) {
                assignedHero = tryAssign(intent.getPreferredHeroId2(), unavailableHeroes, allHeroes);
            }

            // Try Preference 3 if 1 & 2 failed
            if (assignedHero == null) {
                assignedHero = tryAssign(intent.getPreferredHeroId3(), unavailableHeroes, allHeroes);
            }

            // 3. EMERGENCY PICK: If all preferences are banned or already picked
            if (assignedHero == null) {
                assignedHero = findBestMetaHero(intent.getRole(), unavailableHeroes, allHeroes);
                log.warn("Emergency pick triggered for player {} in role {}", intent.getPlayerId(), intent.getRole());
            }

            // Finalize assignment
            finalPicks.put(intent.getPlayerId(), assignedHero);
            unavailableHeroes.add(assignedHero.getId()); // Mark as picked
        }

        return finalPicks;
    }

    private Hero tryAssign(UUID heroId, Set<UUID> unavailable, List<Hero> allHeroes) {
        if (heroId != null && !unavailable.contains(heroId)) {
            return allHeroes.stream()
                    .filter(h -> h.getId().equals(heroId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Finds the highest tier hero (S > A > B...) for a specific role that isn't unavailable.
     */
    private Hero findBestMetaHero(Hero.HeroRole role, Set<UUID> unavailable, List<Hero> allHeroes) {
        return allHeroes.stream()
                .filter(h -> !unavailable.contains(h.getId()))
                .filter(h -> h.getPrimaryRole() == role || h.getSecondaryRole() == role)
                // Sort by MetaTier enum ordinal (S=0, A=1... so natural order is best to worst)
                .min(Comparator.comparingInt(h -> h.getPrimaryTier().ordinal()))
                .orElseThrow(() -> new RuntimeException("No heroes left in the pool for role: " + role));
    }

    /**
     * Internal helper to track which team a pick belongs to during the sort.
     */
    private record DraftEntry(Match.MatchPick pick, boolean isHome) {}
}