package com.dto.ScoreDTO

import io.micronaut.data.annotation.MappedProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class TeamScoreDTO {
    int id

//    @MappedProperty("team_name")
    String name

//    @MappedProperty("total_runs")
    int totalRuns

    TeamScoreDTO(int id, String name, int totalRuns) {
        this.id = id
        this.name = name
        this.totalRuns = totalRuns
    }
}
