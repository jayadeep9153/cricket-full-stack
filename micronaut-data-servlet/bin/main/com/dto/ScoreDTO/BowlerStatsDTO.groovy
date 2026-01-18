package com.dto.ScoreDTO

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class BowlerStatsDTO {
    int id

    String name

    int innings

    double oversBowled

    int runs

    int wickets

    double economy

    BowlerStatsDTO(int id, String name, int innings, double oversBowled,
                   int runs, int wickets, double economy) {
        this.id = id
        this.name = name
        this.innings = innings
        this.oversBowled = oversBowled
        this.runs = runs
        this.wickets = wickets
        this.economy = economy
    }
}
