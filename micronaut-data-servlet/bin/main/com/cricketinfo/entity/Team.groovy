package com.cricketinfo.entity

import io.micronaut.serde.annotation.Serdeable
import io.micronaut.data.annotation.*
import jakarta.persistence.Column
import jakarta.validation.constraints.*

@Serdeable
@MappedEntity("teams")
class Team {
    @Id
    @GeneratedValue(value = GeneratedValue.Type.IDENTITY)
    int id

    @NotBlank
    String name

    Team() {}

    Team(String name) {
        this.name = name
    }
}