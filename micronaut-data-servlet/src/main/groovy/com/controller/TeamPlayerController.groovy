package com.controller

import com.cricketinfo.entity.TeamPlayer
import com.dto.TeamPlayerDTO.CreateTeamPlayerDTO
import com.dto.TeamPlayerDTO.ResponseTeamPlayerDTO
import com.service.TeamPlayerService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Controller("/team-players")
@Validated
class TeamPlayerController {
    final TeamPlayerService teamPlayerService

    TeamPlayerController(TeamPlayerService teamPlayerService){
        this.teamPlayerService = teamPlayerService
    }

    @Get("/")
    List<ResponseTeamPlayerDTO> getAll(){
        return teamPlayerService.getAllTeamPlayers()
    }

    @Get("/{id}")
    TeamPlayer getById(@PathVariable int id){
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerById(id)
        return teamPlayer
    }

    //Get Method - Caching
    @Get("/cache/{id}")
    TeamPlayer getByIdCaching(@PathVariable int id){
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByIdCaching(id)
        return teamPlayer
    }

    @Get("/match/{id}")
    List<ResponseTeamPlayerDTO> getByMatchId(@PathVariable int id){
        return teamPlayerService.getPlayersByMatchId(id)
    }

    @Post("/")
    HttpResponse<?> addTeamPlayer(@Body @Valid CreateTeamPlayerDTO createTeamPlayerDTO){
        TeamPlayer teamPlayer = teamPlayerService.postTeamPlayerMethod(createTeamPlayerDTO)
        return HttpResponse.status(HttpStatus.CREATED).body(teamPlayer)
    }

    @Put("/{id}")
    HttpResponse<?> updateTeamPlayer(@PathVariable int id, @Body @Valid CreateTeamPlayerDTO teamPlayerDTO){
        TeamPlayer teamPlayer = teamPlayerService.putTeamPlayerMethod(id, teamPlayerDTO)
        return HttpResponse.ok([message: "Team Player updated successfully", teamPlayer: teamPlayer])
    }

    //Put method - Caching
    @Put("/cache/{id}")
    HttpResponse<?> updateTeamPlayerCaching(@PathVariable int id, @Body @Valid CreateTeamPlayerDTO teamPlayerDTO){
        TeamPlayer teamPlayer = teamPlayerService.putTeamPlayerCaching(id, teamPlayerDTO)
        return HttpResponse.ok([message: "Team Player updated successfully", teamPlayer: teamPlayer])
    }

    @Delete("/{id}")
    HttpResponse<?> deleteTeamPlayer(@PathVariable int id){
        TeamPlayer teamPlayer = teamPlayerService.deleteTeamPlayerMethod(id)
        return HttpResponse.ok([message: "Match deleted successfully", teamPlayer: teamPlayer])
    }

    //Delete method - Caching
    @Delete("/{id}")
    HttpResponse<?> deleteTeamPlayerCaching(@PathVariable int id){
        TeamPlayer teamPlayer = teamPlayerService.deleteTeamPlayerCaching(id)
        return HttpResponse.ok([message: "Match deleted successfully", teamPlayer: teamPlayer])
    }
}