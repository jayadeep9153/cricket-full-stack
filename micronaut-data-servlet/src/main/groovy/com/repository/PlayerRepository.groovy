package com.repository

import com.cricketinfo.entity.Player
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.MYSQL)
interface PlayerRepository extends CrudRepository<Player, Integer>{
    List<Player> findByNameLike(String name)
    boolean existsByName(String name)
    Optional<Player> findByName(String name)
}