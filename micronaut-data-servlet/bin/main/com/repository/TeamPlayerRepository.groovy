package com.repository

import com.cricketinfo.entity.Team
import com.cricketinfo.entity.TeamPlayer
import io.micronaut.data.annotation.Join
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.MYSQL)
interface TeamPlayerRepository extends CrudRepository<TeamPlayer, Integer>{
    @Join("match")
    @Join("player")
    @Join("team")
    List<TeamPlayer> findByTeam(Team team)

    @Join("match")
    @Join("player")
    @Join("team")
    List<TeamPlayer> findByMatch_Id(int matchId)

    @Join("match")
    @Join("player")
    @Join("team")
    List<TeamPlayer> findAll()

    @Join("match")
    @Join("player")
    @Join("team")
    Optional<TeamPlayer> findById(int id)

    @Join("match")
    @Join("player")
    @Join("team")
    List<TeamPlayer> findByTeam_NameAndMatch_Id(String teamName, int matchId)
}