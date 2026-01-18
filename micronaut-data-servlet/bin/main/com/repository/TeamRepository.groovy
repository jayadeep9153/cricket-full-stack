package com.repository

import com.cricketinfo.entity.Team
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.MYSQL)
interface TeamRepository extends CrudRepository<Team, Integer>{
    boolean existsByName(String name)
    List<Team> findByNameLike(String name)
    Team findByNameIgnoreCase(String name)
    Optional<Team> findByName(String name)
}