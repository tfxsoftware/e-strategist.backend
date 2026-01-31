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
            // --- MID LANERS (Control Mages & Assassins) ---
            createHero("Luxana", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Luxana", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.S, 1.20),
                Hero.HeroRole.SUPPORT, createMeta(0.90, Hero.MetaTier.B, 1.00)
            )),
            createHero("Ignis", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Ignis", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.A, 1.10)
            )),
            createHero("Vortex", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Vortex", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.S, 1.25),
                Hero.HeroRole.JUNGLE, createMeta(0.80, Hero.MetaTier.C, 0.90)
            )),
            createHero("Aurelia", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Aurelia", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.A, 1.15)
            )),
            createHero("Zenith", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Zenith", Map.of(
                Hero.HeroRole.MID, createMeta(1.00, Hero.MetaTier.B, 1.00)
            )),

            // --- JUNGLERS (Assassins & Tanks) ---
            createHero("Storm Spirit", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Storm", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.S, 1.25),
                Hero.HeroRole.TOP, createMeta(0.75, Hero.MetaTier.D, 0.80)
            )),
            createHero("Shadow Stalker", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Shadow", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.A, 1.15)
            )),
            createHero("Fenris", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Fenris", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.S, 1.20),
                Hero.HeroRole.TOP, createMeta(0.90, Hero.MetaTier.B, 1.05)
            )),
            createHero("Jade", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Jade", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.B, 1.00),
                Hero.HeroRole.SUPPORT, createMeta(0.85, Hero.MetaTier.C, 0.90)
            )),
            createHero("Kraken", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Kraken", Map.of(
                Hero.HeroRole.JUNGLE, createMeta(1.00, Hero.MetaTier.A, 1.10),
                Hero.HeroRole.SUPPORT, createMeta(0.70, Hero.MetaTier.D, 0.80)
            )),

            // --- CARRYS (Marksmen) ---
            createHero("Vail", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Vail", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.S, 1.15)
            )),
            createHero("Bolt", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Bolt", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.B, 1.00)
            )),
            createHero("Cinder", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Cinder", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.A, 1.10),
                Hero.HeroRole.MID, createMeta(0.80, Hero.MetaTier.C, 0.95)
            )),
            createHero("Riptide", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Riptide", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.S, 1.18)
            )),
            createHero("Ghost", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Ghost", Map.of(
                Hero.HeroRole.CARRY, createMeta(1.00, Hero.MetaTier.A, 1.12)
            )),

            // --- TOP LANERS (Bruisers & Tanks) ---
            createHero("IronClad", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Iron", Map.of(
                Hero.HeroRole.TOP, createMeta(1.00, Hero.MetaTier.A, 1.10),
                Hero.HeroRole.JUNGLE, createMeta(0.85, Hero.MetaTier.B, 1.00)
            )),
            createHero("Goliath", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Goliath", Map.of(
                Hero.HeroRole.TOP, createMeta(1.00, Hero.MetaTier.S, 1.20)
            )),
            createHero("Atlas", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Atlas", Map.of(
                Hero.HeroRole.TOP, createMeta(1.00, Hero.MetaTier.A, 1.12),
                Hero.HeroRole.SUPPORT, createMeta(0.80, Hero.MetaTier.B, 1.00)
            )),
            createHero("Katarina", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Kat", Map.of(
                Hero.HeroRole.TOP, createMeta(1.00, Hero.MetaTier.S, 1.22),
                Hero.HeroRole.MID, createMeta(0.85, Hero.MetaTier.C, 0.95)
            )),

            // --- SUPPORTS (Enchanters & Engagers) ---
            createHero("Seraphina", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Sera", Map.of(
                Hero.HeroRole.SUPPORT, createMeta(1.00, Hero.MetaTier.S, 1.20)
            )),
            createHero("Thorn", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Thorn", Map.of(
                Hero.HeroRole.SUPPORT, createMeta(1.00, Hero.MetaTier.A, 1.10),
                Hero.HeroRole.TOP, createMeta(0.60, Hero.MetaTier.D, 0.70)
            )),
            createHero("Echo", "https://api.dicebear.com/7.x/pixel-art/svg?seed=Echo", Map.of(
                Hero.HeroRole.SUPPORT, createMeta(1.00, Hero.MetaTier.B, 1.00),
                Hero.HeroRole.MID, createMeta(0.70, Hero.MetaTier.D, 0.85)
            ))
        );

        seedHeroes.forEach(this::upsertHero);
        log.info("Hero synchronization complete. Total heroes in pool: {}", heroRepository.count());
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