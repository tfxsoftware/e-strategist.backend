package com.tfxsoftware.memserver.modules.matches.dto;

import com.tfxsoftware.memserver.modules.heroes.Hero.HeroRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMatchDraftDto {
    @NotNull
    private List<UUID> teamBans;
    @NotNull
    private List<MatchPickDto> pickIntentions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchPickDto {
        @NotNull
        private UUID playerId;
        @NotNull
        private HeroRole role;
        @NotNull
        private UUID preferredHeroId1;
        @NotNull
        private UUID preferredHeroId2;
        @NotNull
        private UUID preferredHeroId3;
        @NotNull
        private Integer pickOrder;
    }
}

