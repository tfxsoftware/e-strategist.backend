package com.tfxsoftware.memserver.modules.heroes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Professional Data Seeding using an Upsert (Update or Insert) strategy.
 * Fixed: Uses mutable HashMaps to avoid UnsupportedOperationException on updates.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HeroDataInitializer implements CommandLineRunner {

    private final HeroRepository heroRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting Hero data synchronization...");

        // We wrap the seed data in mutable structures
        List<Hero> seedHeroes = List.of(
            createHero("Luxana", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Luxana", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.S, 1.20),
                Hero.HeroRole.SUPPORT, createMeta(0.90, Hero.MetaTier.B, 1.00)
            )),
            createHero("Storm Spirit", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Storm", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.S, 1.25),
                Hero.HeroRole.TOP, createMeta(0.75, Hero.MetaTier.D, 0.80)
            )),
            createHero("Vail", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Vail", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.S, 1.15)
            )),
            createHero("IronClad", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Iron", Map.of(
                Hero.HeroRole.TOP, createMeta(1.00, Hero.MetaTier.A, 1.10),
                Hero.HeroRole.JUNGLE, createMeta(0.85, Hero.MetaTier.B, 1.00)
            ))
        );

        seedHeroes.forEach(this::upsertHero);
        
        log.info("Hero data synchronization complete.");
    }

    private void upsertHero(Hero seedHero) {
        heroRepository.findByName(seedHero.getName()).ifPresentOrElse(
            existingHero -> {
                log.info("Updating hero: {}", existingHero.getName());
                existingHero.setPictureUrl(seedHero.getPictureUrl());
                
                // FIX: Instead of clear() which fails on immutable maps, 
                // we replace the reference with a new mutable HashMap.
                // Hibernate will handle the collection orphan removal automatically.
                existingHero.setRoleSettings(new HashMap<>(seedHero.getRoleSettings()));
                
                heroRepository.save(existingHero);
            },
            () -> {
                log.info("Creating new hero: {}", seedHero.getName());
                heroRepository.save(seedHero);
            }
        );
    }

    private Hero createHero(String name, String url, Map<Hero.HeroRole, Hero.RoleMetadata> settings) {
        return Hero.builder()
                .name(name)
                .pictureUrl(url)
                // Ensure the initial map is mutable
                .roleSettings(new HashMap<>(settings))
                .build();
    }

    private Hero.RoleMetadata createMeta(double eff, Hero.MetaTier tier, double mult) {
        return Hero.RoleMetadata.builder()
                .efficiency(BigDecimal.valueOf(eff))
                .tier(tier)
                .multiplier(BigDecimal.valueOf(mult))
                .build();
    }
}