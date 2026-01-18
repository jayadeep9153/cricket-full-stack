package com.controller

import com.cricketinfo.entity.*
import com.dto.TeamDTO.*
import com.service.*
import groovy.util.logging.Slf4j
import io.micronaut.http.*
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Validated
@Controller("/teams")
@Slf4j
class TeamController {
    final TeamService teamService

    TeamController(TeamService teamService){
        this.teamService = teamService
    }

    @Get("/{id}")
    Team getById(@PathVariable int id){
        Team teamId = teamService.getTeamById(id)
        return teamId
    }

    //Get - Caching
    @Get("/cache/{id}")
    Team getByIdCaching(@PathVariable int id){
        Team team = teamService.getTeamByIdCaching(id)
        return team
    }

    @Get("/search")
    List<Team> getTeamBySearch(@QueryValue String name){
        List<Team> teams = teamService.searchByTeam(name)
        return teams
    }

    @Get("/{name}/players")
    List<Player> getPlayersFromTeamName(@PathVariable String name){
        List<Player> players = teamService.playersFromTeamName(name)
        return players
    }

    @Get("/")
    List<Team> getAll(){
        return teamService.getAllTeams()
    }

    @Post("/")
    HttpResponse<?> addTeam(@Body @Valid CreateTeamDTO createTeamDTO){
        Team team = teamService.postTeamMethod(createTeamDTO)
        return HttpResponse.status(HttpStatus.CREATED).body(team)
    }

    @Put("/{id}")
    HttpResponse<?> updateTeam(@PathVariable int id, @Body @Valid CreateTeamDTO createTeamDTO){
        Team team = teamService.putTeamMethod(id, createTeamDTO)
        return HttpResponse.ok([message: "Team successfully updated", team: team])
    }

    //Put - Caching
    @Put("/cache/{id}")
    HttpResponse<?> updateTeamCaching(@PathVariable int id, @Body @Valid CreateTeamDTO createTeamDTO){
        Team team = teamService.putTeamMethodCaching(id, createTeamDTO)
        return HttpResponse.ok([message: "Team successfully updated", team: team])
    }

    @Delete("/{id}")
    HttpResponse<?> deleteTeam(@PathVariable int id){
        Team deletedTeam = teamService.deleteTeamMethod(id)
        return HttpResponse.ok([message: "Team deleted successfully", team: deletedTeam])
    }

    //Delete Caching
    @Delete("/cache/{id}")
    HttpResponse<?> deleteTeamCaching(@PathVariable int id){
        Team deleteTeam = teamService.deleteTeamCaching(id)
        return HttpResponse.ok([message: "Team deleted successfully", team: deleteTeam])
    }
}