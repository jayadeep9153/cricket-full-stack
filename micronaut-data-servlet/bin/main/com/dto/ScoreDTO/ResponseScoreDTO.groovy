package com.dto.ScoreDTO

import com.cricketinfo.entity.*
import com.dto.Match.ResponseMatchDTO
import com.dto.PlayerDTO.ResponsePlayerDTO
import com.dto.TeamDTO.ResponseTeamDTO
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class ResponseScoreDTO {
    int id
    ResponseMatchDTO match
    int innings
    ResponseTeamDTO battingTeam
    ResponseTeamDTO bowlingTeam
    int overNumber
    int ballNumber
    ResponsePlayerDTO striker
    ResponsePlayerDTO nonStriker
    ResponsePlayerDTO bowler
    int runsScored
    ExtraType extraType
    boolean isWicket
    WicketType wicketType
    ResponsePlayerDTO fielder

    ResponseScoreDTO(Score score){
        this.id = score.id
        this.match = new ResponseMatchDTO(score.match)
        this.innings = score.innings
        this.battingTeam = new ResponseTeamDTO(score.battingTeam)
        this.bowlingTeam = new ResponseTeamDTO(score.bowlingTeam)
        this.overNumber = score.overNumber
        this.ballNumber = score.battingTeam
        this.striker = new ResponsePlayerDTO(score.striker)
        this.nonStriker = new ResponsePlayerDTO(score.nonStriker)
        this.bowler = new ResponsePlayerDTO(score.bowler)
        this.runsScored = score.runsScored
        this.extraType = score.extraType
        this.isWicket = score.isWicket
        this.wicketType = score.wicketType
        this.fielder = new ResponsePlayerDTO(score.fielder)
    }
}