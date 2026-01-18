package com.dto.ScoreDTO

import io.micronaut.data.annotation.MappedProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class PlayerStatsDTO {
    Long id

    @MappedProperty("player_name")
    String playerName

    @MappedProperty("total_runs")
    Long totalRuns

    @MappedProperty("balls_played")
    Long ballsPlayed

    Long fours

    Long sixes

    @MappedProperty("strike_rate")
    Double strikeRate

    PlayerStatsDTO(Long id, String playerName, Long totalRuns,
                   Long ballsPlayed, Long fours, Long sixes, Double strikeRate) {
        this.id = id
        this.playerName = playerName
        this.totalRuns = totalRuns
        this.ballsPlayed = ballsPlayed
        this.fours = fours
        this.sixes = sixes
        this.strikeRate = strikeRate
    }
}
