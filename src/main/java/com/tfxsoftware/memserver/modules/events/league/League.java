package com.tfxsoftware.memserver.modules.events.league;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tfxsoftware.memserver.modules.events.Event;

@Entity
@Table(name = "leagues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class League {
    @Id
    private UUID eventId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "event_id")
    private Event event;

    private Integer roundRobinCount;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LeagueStanding> standings = new ArrayList<>();
}

