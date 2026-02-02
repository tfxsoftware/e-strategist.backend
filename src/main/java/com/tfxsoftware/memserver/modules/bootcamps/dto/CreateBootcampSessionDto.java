package com.tfxsoftware.memserver.modules.bootcamps.dto;

import com.tfxsoftware.memserver.modules.heroes.Hero.HeroRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Main wrapper for starting a bootcamp session.
 * Using records for conciseness and immutability.
 */
public record CreateBootcampSessionDto(
    @NotEmpty(message = "Bootcamp must include at least one player configuration")
    @Valid
    List<PlayerTrainingConfigDto> configs
) {
    /**
     * Individual training configuration for a player.
     */
    public record PlayerTrainingConfigDto(
        @NotNull(message = "Player ID is required")
        UUID playerId,

        @NotNull(message = "Target role is required")
        HeroRole targetRole,

        @NotNull(message = "Primary hero is required")
        UUID primaryHeroId,

        UUID secondaryHeroId1,
        UUID secondaryHeroId2
    ) {}
}