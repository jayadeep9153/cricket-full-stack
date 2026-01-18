package com.dto.ScoreDTO

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class TeamWicketsDTO {
    int id

    String name

    int totalWickets

    TeamWicketsDTO(int id, String name, int totalWickets) {
        this.id = id
        this.name = name
        this.totalWickets = totalWickets
    }
}
