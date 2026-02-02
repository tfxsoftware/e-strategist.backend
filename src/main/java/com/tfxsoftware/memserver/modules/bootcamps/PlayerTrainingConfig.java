package com.tfxsoftware.memserver.modules.bootcamps;

import java.util.UUID;

import com.tfxsoftware.memserver.modules.heroes.Hero.HeroRole;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "player_training_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerTrainingConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private BootcampSession session;

    @Column(nullable = false)
    private UUID playerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HeroRole targetRole;

    @Column(nullable = false)
    private UUID primaryHeroId;

    private UUID secondaryHeroId1;
    private UUID secondaryHeroId2;
}