package com.tfxsoftware.memserver.modules.matches;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "match_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResult {

    @Id
    private UUID matchId; // Shared ID with the Match entity (OneToOne)

    @Column(nullable = false)
    private UUID winnerRosterId;

    // --- Team Performance Overviews ---
    @Column(precision = 10, scale = 2)
    private BigDecimal homeTotalPerformance;

    @Column(precision = 10, scale = 2)
    private BigDecimal awayTotalPerformance;

    // will work on this later, were generating custom description, for now just leave it null
    private String description;

    /**
     * The "Box Score": Detailed stats for every player in the match.
     * Stores: PlayerID -> { performancePoints, heroId, role }
     * Using JSONB for maximum flexibility in the match history view.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> playerStats;


}