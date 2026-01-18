package com.dto.PlayerDTO

import com.cricketinfo.entity.Role
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*

@Serdeable
class CreatePlayerDTO {
    @NotBlank(message = "Player name must not be empty")
    String name

    @NotNull
    @Min(value = 15)
    int age

    @NotNull
    Role role
}
