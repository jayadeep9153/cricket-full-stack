package com.dto.ScoreDTO

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class AllTeamScoresDTO {
    int matchId

    int id

//    @MappedProperty("team_name")
    String name

//    @MappedProperty("total_runs")
    int totalRuns

    AllTeamScoresDTO(int matchId, int id, String name, int totalRuns) {
        this.matchId = matchId
        this.id = id
        this.name = name
        this.totalRuns = totalRuns
    }
}
