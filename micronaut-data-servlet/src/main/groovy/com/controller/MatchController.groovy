package com.controller

import com.cricketinfo.entity.*
import com.dto.Match.*
import com.dto.ScoreDTO.*
import com.service.*
import io.micronaut.http.*
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Controller("/matches")
@Validated
class MatchController {
    final MatchService matchService

    MatchController(MatchService matchService){
        this.matchService = matchService
    }

    @Get("/")
    List<ResponseMatchDTO> getAll(){
        return matchService.getAllMatches()
    }

    @Get("/{id}")
    ResponseMatchDTO getById(@PathVariable int id){
        ResponseMatchDTO matchId = matchService.getMatchById(id)
        return matchId
    }

    //Get method - Caching
    @Get("/{id}")
    ResponseMatchDTO getByIdCaching(@PathVariable int id){
        ResponseMatchDTO matchId = matchService.getMatchByIdCaching(id)
        return matchId
    }

    @Get("/scores")
    List<AllTeamScoresDTO> getAllScores(){
        return matchService.getAllTeamsScores()
    }

    @Get("/scores/{id}")
    List<TeamScoreDTO> getScoresByMatchId(@PathVariable int id){
        return matchService.getScoresByMatchId(id)
    }

    @Get("/wickets")
    List<AllTeamsWicketsDTO> getAllWickets(){
        return matchService.getAllTeamsWickets()
    }

    @Get("/wickets/{id}")
    List<TeamWicketsDTO> getWicketsByMatchId(@PathVariable int id){
        return matchService.getWicketsByMatchId(id)
    }

    @Get("/player-stats/{id}")
    List<PlayerStatsDTO> getPlayerStatsByMatchId(@PathVariable int id){
        return matchService.getPlayerStatsByMatchId(id)
    }

    @Get("/bowler-stats/{id}")
    List<BowlerStatsDTO> getBowlerStatsByMatchId(@PathVariable int id){
        return matchService.getBowlerStatsByMatchId(id)
    }

    @Post("/")
    HttpResponse<?> addMatch(@Body @Valid CreateMatchDTO matchDTO){
        Match match = matchService.postMatchMethod(matchDTO)
        return HttpResponse.status(HttpStatus.CREATED).body(match)
    }

    @Put("/{id}")
    HttpResponse<?> updateMatch(@PathVariable int id, @Body @Valid CreateMatchDTO matchDTO){
        Match match = matchService.putMatchMethod(id, matchDTO)
        return HttpResponse.ok([message: "Match updated successfully", match: match])
    }

    @Delete("/{id}")
    HttpResponse<?> deleteMatch(@PathVariable int id){
        Match match = matchService.deleteMatchMethod(id)
        return HttpResponse.ok([message: "Match deleted successfully", match: match])
    }
}