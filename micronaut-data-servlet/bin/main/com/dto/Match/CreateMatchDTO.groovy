package com.dto.Match

import com.cricketinfo.entity.Toss
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.*
import java.time.LocalDate

@Serdeable
class CreateMatchDTO {
    @NotBlank(message = "Team name should not be empty")
    String team1Name

    @NotBlank(message = "Team name should not be empty")
    String team2Name

    @NotNull
    LocalDate date

    @NotBlank(message = "Toss winner should not be empty")
    String tossWinner

    @NotNull
    Toss tossDecision

    @NotBlank(message = "Match winner should not be empty")
    String matchWinner

    @NotBlank(message = "Player of the match should not be empty")
    String potm
}
