package com.dto.Match

import com.cricketinfo.entity.Match
import com.cricketinfo.entity.Player
import com.cricketinfo.entity.Toss
import com.dto.TeamDTO.ResponseTeamDTO
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate

@Serdeable
class ResponseMatchDTO {
    int id
    ResponseTeamDTO team1
    ResponseTeamDTO team2
    LocalDate date
    ResponseTeamDTO tossWinner
    Toss tossDecision
    ResponseTeamDTO matchWinner
    Player potm

    ResponseMatchDTO(Match match) {
        this.id = match.id
        this.team1 = new ResponseTeamDTO(match.team1)
        this.team2 = new ResponseTeamDTO(match.team2)
        this.date = match.date
        this.tossWinner = new ResponseTeamDTO(match.tossWinner)
        this.tossDecision = match.tossDecision
        this.matchWinner = new ResponseTeamDTO(match.matchWinner)
        this.potm = match.potm
    }
}