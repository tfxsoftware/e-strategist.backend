package com.tfxsoftware.memserver.modules.heroes.dto;

import com.tfxsoftware.memserver.modules.heroes.Hero.HeroRole;
import com.tfxsoftware.memserver.modules.heroes.Hero.MetaTier;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for sending Hero data to the frontend.
 * Mirrors the multi-role metadata structure of the Entity.
 */
@Data
@Builder
public class HeroResponse {
    private UUID id;
    private String name;
    private String pictureUrl;
    private Map<HeroRole, RoleMetadataResponse> roleSettings;

    @Data
    @Builder
    public static class RoleMetadataResponse {
        private BigDecimal efficiency;
        private MetaTier tier;
        private BigDecimal multiplier;
    }
}