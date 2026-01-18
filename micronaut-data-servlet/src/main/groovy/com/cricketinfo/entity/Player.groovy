package com.cricketinfo.entity

import io.micronaut.data.annotation.*
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*

@Serdeable
@MappedEntity("players")
class Player {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    int id

    @NotBlank
    String name

    int age

    Role role

    Player() {}

    Player(String name, int age, Role role) {
        this.name = name
        this.age = age
        this.role = role
    }
}
