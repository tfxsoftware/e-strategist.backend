package com.tfxsoftware.memserver.modules.heroes;

import com.tfxsoftware.memserver.modules.heroes.dto.HeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    /**
     * Fetches all heroes and transforms them into DTOs.
     */
    public List<HeroResponse> getAllHeroes() {
        return heroRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to transform the Hero entity (and its nested map) 
     * into the HeroResponse DTO.
     */
    private HeroResponse mapToResponse(Hero hero) {
        // Transform the internal Entity Map to the DTO Map
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
                // Mapping the optional field. If it's null in the DB, it will be null in the JSON.
                .pictureUrl(hero.getPictureUrl())
                .roleSettings(roleSettings)
                .build();
    }
}