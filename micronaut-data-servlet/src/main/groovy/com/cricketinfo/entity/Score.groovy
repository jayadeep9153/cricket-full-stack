package com.cricketinfo.entity

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.*
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*

@Serdeable
@MappedEntity("score")
class Score {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Integer id

    @NotNull
    @MappedProperty("match_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    Match match

    @NotNull
    @MappedProperty("innings")
    Integer innings

    @NotNull
    @MappedProperty("batting_team_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team battingTeam

    @NotNull
    @MappedProperty("bowling_team_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team bowlingTeam

    @Nullable
    @MappedProperty("over_no")
    Integer overNumber

    @Nullable
    @MappedProperty("ball_no")
    Integer ballNumber

    @NotNull
    @MappedProperty("striker_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    Player striker

    @NotNull
    @MappedProperty("non_striker_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    Player nonStriker

    @NotNull
    @MappedProperty("bowler_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    Player bowler

    @Nullable
    @MappedProperty("runs_scored")
    Integer runsScored

    @Nullable
    @MappedProperty("extra_type")
    ExtraType extraType

    @Nullable
    @MappedProperty("extra_runs")
    Integer extraRuns

    @Nullable
    @MappedProperty("is_wicket")
    Boolean isWicket

    @MappedProperty("wicket_type")
    WicketType wicketType

    @Nullable
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    @MappedProperty("fielder_id")
    Player fielder

    Score(Integer id, Match match, Integer innings, Team battingTeam, Team bowlingTeam,
          @Nullable Integer overNumber, @Nullable Integer ballNumber, Player striker, Player nonStriker,
          Player bowler, @Nullable Integer runsScored, @Nullable ExtraType extraType, @Nullable Integer extraRuns,
          @Nullable Boolean isWicket, WicketType wicketType, @Nullable Player fielder) {
        this.id = id
        this.match = match
        this.innings = innings
        this.battingTeam = battingTeam
        this.bowlingTeam = bowlingTeam
        this.overNumber = overNumber
        this.ballNumber = ballNumber
        this.striker = striker
        this.nonStriker = nonStriker
        this.bowler = bowler
        this.runsScored = runsScored
        this.extraType = extraType
        this.extraRuns = extraRuns
        this.isWicket = isWicket
        this.wicketType = wicketType
        this.fielder = fielder
    }
}