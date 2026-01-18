package com.cricketinfo.entity

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.*
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotNull

@Serdeable
@MappedEntity("team_player")
class TeamPlayer {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    int id

    @NotNull
    @MappedProperty("team_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Team team

    @NotNull
    @MappedProperty("match_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    Match match

    @NotNull
    @MappedProperty("player_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE, cascade = [Relation.Cascade.UPDATE])
    Player player

    @MappedProperty("is_captain")
    boolean isCaptain

    @MappedProperty("is_wicket_keeper")
    boolean isWicketKeeper

//    TeamPlayer() {}

    TeamPlayer(Team team, Match match, Player player, boolean isCaptain, boolean isWicketKeeper) {
        this.team = team
        this.match = match
        this.player = player
        this.isCaptain = isCaptain
        this.isWicketKeeper = isWicketKeeper
    }
}