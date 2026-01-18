package com.dto.TeamDTO

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*

@Serdeable
class CreateTeamDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    String name
}