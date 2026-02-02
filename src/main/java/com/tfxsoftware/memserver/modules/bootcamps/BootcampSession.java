package com.tfxsoftware.memserver.modules.bootcamps;

import com.tfxsoftware.memserver.modules.rosters.Roster;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an active training event for a Roster.
 */
@Entity
@Table(name = "bootcamp_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampSession {
    @Id
    private UUID rosterId; // OneToOne with Roster, using Roster's ID as PK

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "roster_id")
    private Roster roster;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime lastTickAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlayerTrainingConfig> playerConfigs = new ArrayList<>();
}