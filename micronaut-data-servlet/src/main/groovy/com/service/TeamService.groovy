package com.service

import com.caching.HazelcastClientConfig
import com.cricketinfo.entity.*
import com.dto.TeamDTO.CreateTeamDTO
import com.repository.TeamRepository
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton

@Singleton
@Slf4j
class TeamService {
    final TeamRepository teamRepository
    final TeamPlayerService teamPlayerService
    final HazelcastClientConfig clientConfig

    TeamService(TeamRepository teamRepository, TeamPlayerService teamPlayerService,
                HazelcastClientConfig clientConfig) {
        this.teamRepository = teamRepository
        this.teamPlayerService = teamPlayerService
        this.clientConfig = clientConfig
    }

    //Get - getById
    Team getTeamById(int id){
        Team teamId = teamRepository.findById(id).orElse(null)
        if(!teamId){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team found with ${id}".toString())
        }
        return teamId
    }

    //Get Caching - getByIdCaching
    Team getTeamByIdCaching(int id){
        Team team = (Team) clientConfig.getValue(id.toString(), 'teams'){
            log.info("Inside fetcher closure for team ${id}")
            Team result = teamRepository.findById(id).orElse(null)
            log.info("Repository returned")
            return result
        }
        if(!team){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team found with ${id}".toString())
        }
        return team
    }

    //Get - search
    List<Team> searchByTeam(String name){
        List<Team> teams = teamRepository.findByNameLike(name)
        if(!teams){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team found with name ${name}".toString())
        }
        return teams
    }

    List<Player> playersFromTeamName(String name){
        List<Team> team = teamRepository.findByNameLike(name)
        if(!team){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team found with name ${name}".toString())
        }

        List<Player> players = teamPlayerService.getPlayerByTeamName(name)
        return players
    }

    List<Team> getAllTeams(){
        return teamRepository.findAll()
    }

    Team postTeamMethod(CreateTeamDTO createTeamDTO){
        try{
            if(teamRepository.existsByName(createTeamDTO.name)){
                throw new HttpStatusException(HttpStatus.CONFLICT, "Team ${createTeamDTO.name} already exists".toString())
            }

            Team team = new Team(createTeamDTO.name)
            return teamRepository.save(team)
        } catch(HttpStatusException e) {
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inserting team: ${e.message}")
        }
    }

    Team putTeamMethod(int id, CreateTeamDTO createTeamDTO){
        Team existingTeam = teamRepository.findById(id).orElse(null)
        if(!existingTeam){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Team with id ${id} not found".toString())
        }

        if(existingTeam.name == createTeamDTO.name){
            throw new HttpStatusException(HttpStatus.CONFLICT, "No changes made, team: ${existingTeam.name}".toString())
        }

        if(teamRepository.existsByName(createTeamDTO.name) && createTeamDTO.name != existingTeam.name){
            throw new HttpStatusException(HttpStatus.CONFLICT, "Team ${createTeamDTO.name} already exists".toString())
        }
        existingTeam.name = createTeamDTO.name
        return teamRepository.update(existingTeam)
    }

    //Put Caching- putTeamMethodCaching
    Team putTeamMethodCaching(int id, CreateTeamDTO createTeamDTO){
        Team existingTeam = teamRepository.findById(id).orElse(null)
        if(!existingTeam){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Team with id ${id} not found".toString())
        }

        if(existingTeam.name == createTeamDTO.name){
            throw new HttpStatusException(HttpStatus.CONFLICT, "No changes made, team: ${existingTeam.name}".toString())
        }

        if(teamRepository.existsByName(createTeamDTO.name) && createTeamDTO.name != existingTeam.name){
            throw new HttpStatusException(HttpStatus.CONFLICT, "Team ${createTeamDTO.name} already exists".toString())
        }
        existingTeam.name = createTeamDTO.name
        Team updatedTeam = teamRepository.update(existingTeam)
        clientConfig.putValue(id.toString(), 'teams', updatedTeam)
        log.info("Update team with id ${id}")
        return updatedTeam
    }

    Team deleteTeamMethod(int id){
        Team existingTeam = teamRepository.findById(id).orElse(null)
        if(!existingTeam){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Team with id ${id} not found".toString())
        }
        try{
            teamRepository.deleteById(id)
            return existingTeam
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting team: ${e.message}".toString())
        }
    }

    //Delete Caching - deleteTeamCaching
    Team deleteTeamCaching(int id){
        Team existingTeam = teamRepository.findById(id).orElse(null)
        if(!existingTeam){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Team with id ${id} not found".toString())
        }
        try{
            teamRepository.deleteById(id)
            clientConfig.evict(id.toString(), 'teams')
            log.info("Deleted team with id ${id}")
            return existingTeam
        } catch (Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting team: ${e.message}".toString())
        }
    }
}