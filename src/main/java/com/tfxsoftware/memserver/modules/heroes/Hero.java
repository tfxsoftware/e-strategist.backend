package com.tfxsoftware.memserver.modules.heroes;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "heroes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String pictureUrl;

    /**
     * FIX: Added FetchType.EAGER.
     * This ensures the roleSettings are loaded into memory immediately
     * when the Hero is fetched, preventing "No Session" errors in the Service.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hero_role_metadata", joinColumns = @JoinColumn(name = "hero_id"))
    @MapKeyColumn(name = "role")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<HeroRole, RoleMetadata> roleSettings;

    public enum HeroRole {
        TOP, JUNGLE, MID, CARRY, SUPPORT
    }

    public enum MetaTier {
        S, A, B, C, D
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoleMetadata {
        private BigDecimal efficiency;
        private MetaTier tier;
        private BigDecimal multiplier;
    }

        /**
     * Helper for the Match Engine to get efficiency.
     */
    public BigDecimal getEfficiencyForRole(HeroRole role) {
        if (roleSettings == null || !roleSettings.containsKey(role)) return BigDecimal.ZERO;
        return roleSettings.get(role).getEfficiency();
    }

    /**
     * Helper for the Match Engine to get the Meta Multiplier.
     */
    public BigDecimal getMultiplierForRole(HeroRole role) {
        if (roleSettings == null || !roleSettings.containsKey(role)) return BigDecimal.ONE;
        return roleSettings.get(role).getMultiplier();
    }
}