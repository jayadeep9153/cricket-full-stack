package com.cricketinfo.entity

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.*
import io.micronaut.serde.annotation.*
import jakarta.validation.constraints.*

import java.time.LocalDate

@Serdeable
@MappedEntity("matches")
class Match {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Integer id

    @NotNull
    @MappedProperty("team1_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team team1

    @NotNull
    @MappedProperty("team2_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team team2

    @Nullable
    @MappedProperty("match_date")
    LocalDate date

    @Nullable
    @MappedProperty("toss_winner_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team tossWinner

    @NotNull
    @MappedProperty("toss_decision")
    Toss tossDecision

    @Nullable
    @MappedProperty("match_winner_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team matchWinner

    @Nullable
    @MappedProperty("player_of_the_match")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Player potm

    Match() {}

    Match(Team team1, Team team2, @Nullable LocalDate date, @Nullable Team tossWinner,
          @Nullable Toss tossDecision, @Nullable Team matchWinner, @Nullable Player potm) {
        this.team1 = team1
        this.team2 = team2
        this.date = date
        this.tossWinner = tossWinner
        this.tossDecision = tossDecision
        this.matchWinner = matchWinner
        this.potm = potm
    }
}