package com.tfxsoftware.memserver.modules.heroes;

import com.tfxsoftware.memserver.modules.heroes.dto.HeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    /**
     * FIX: Added @Transactional(readOnly = true).
     * This keeps the database session open while the mapToResponse 
     * logic iterates over the roleSettings collection.
     */
    @Transactional(readOnly = true)
    public List<HeroResponse> getAllHeroes() {
        return heroRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HeroResponse mapToResponse(Hero hero) {
        Map<Hero.HeroRole, HeroResponse.RoleMetadataResponse> roleSettings = hero.getRoleSettings().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> HeroResponse.RoleMetadataResponse.builder()
                                .efficiency(entry.getValue().getEfficiency())
                                .tier(entry.getValue().getTier())
                                .multiplier(entry.getValue().getMultiplier())
                                .build()
                ));

        return HeroResponse.builder()
                .id(hero.getId())
                .name(hero.getName())
                .pictureUrl(hero.getPictureUrl())
                .roleSettings(roleSettings)
                .build();
    }
}