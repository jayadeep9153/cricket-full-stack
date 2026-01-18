package com.repository

import com.cricketinfo.entity.*
import io.micronaut.data.annotation.Join
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.MYSQL)
interface MatchRepository extends CrudRepository<Match, Integer>{
    @Join("team1")
    @Join("team2")
    @Join("tossWinner")
    @Join("matchWinner")
    @Join("potm")
    List<Match> findAll()

    @Join("team1")
    @Join("team2")
    @Join("tossWinner")
    @Join("matchWinner")
    @Join("potm")
    Optional<Match> findById(int id)
}