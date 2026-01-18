package com.dto.TeamPlayerDTO

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*

@Serdeable
class CreateTeamPlayerDTO {
    @NotBlank(message = "Team name should not be empty")
    String team

    @NotNull(message = "Match name should not be empty")
    int matchId

    @NotBlank(message = "Player nane should not be empty")
    String player

    boolean isCaptain

    boolean isWicketKeeper
}