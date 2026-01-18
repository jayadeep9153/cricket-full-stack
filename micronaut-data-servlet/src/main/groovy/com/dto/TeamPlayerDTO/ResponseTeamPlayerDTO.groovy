package com.dto.TeamPlayerDTO

import com.cricketinfo.entity.TeamPlayer
import com.dto.PlayerDTO.ResponsePlayerDTO
import com.dto.TeamDTO.ResponseTeamDTO
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class ResponseTeamPlayerDTO {
    int id
    ResponseTeamDTO team
    int matchId
    ResponsePlayerDTO player
    boolean isCaptain
    boolean isWicketKeeper

    ResponseTeamPlayerDTO(TeamPlayer teamPlayer) {
        this.id = teamPlayer.id
        this.team = new ResponseTeamDTO(teamPlayer.team)
        this.matchId = teamPlayer.match.id
        this.player = new ResponsePlayerDTO(teamPlayer.player)
        this.isCaptain = teamPlayer.isCaptain
        this.isWicketKeeper = teamPlayer.isWicketKeeper
    }
}