package com.tfxsoftware.memserver.modules.events.league;

import com.tfxsoftware.memserver.modules.rosters.Roster;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "league_standings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueStanding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_event_id")
    private League league;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roster_id")
    private Roster roster;

    private Integer position;
    private Integer wins;
    private Integer losses;
}
