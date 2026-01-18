package com.dto.ScoreDTO

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class AllTeamsWicketsDTO {
    int matchId

    int id

    String name

    int totalWickets

    AllTeamsWicketsDTO(int matchId, int id, String name, int totalWickets) {
        this.matchId = matchId
        this.id = id
        this.name = name
        this.totalWickets = totalWickets
    }
}