package com.service

import com.caching.HazelcastClientConfig
import com.cricketinfo.entity.*
import com.dto.TeamPlayerDTO.CreateTeamPlayerDTO
import com.dto.TeamPlayerDTO.ResponseTeamPlayerDTO
import com.repository.*
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton

@Singleton
@Slf4j
class TeamPlayerService {
    final TeamRepository teamRepository
    final TeamPlayerRepository teamPlayerRepository
    final PlayerRepository playerRepository
    final MatchRepository matchRepository
    final HazelcastClientConfig clientConfig

    TeamPlayerService(TeamRepository teamRepository, TeamPlayerRepository teamPlayerRepository,
                      PlayerRepository playerRepository, MatchRepository matchRepository, HazelcastClientConfig clientConfig){
        this.teamRepository = teamRepository
        this.teamPlayerRepository = teamPlayerRepository
        this.playerRepository = playerRepository
        this.matchRepository = matchRepository
        this.clientConfig = clientConfig
    }

    List<Player> getPlayerByTeamName(String teamName){
        Team team = teamRepository.findByNameIgnoreCase(teamName)
        if(team == null)    return []

        log.info("The team from team repository is ${team.name}")

        List<Player> players = teamPlayerRepository
                .findByTeam(team)
                .findAll {it.player != null}
                .collect {it.player}

        log.info("The players from teams are ${players}")
        return players
    }

    List<ResponseTeamPlayerDTO> getAllTeamPlayers(){
        return teamPlayerRepository.findAll().collect {new ResponseTeamPlayerDTO(it)}
    }

    TeamPlayer getTeamPlayerById(int id){
        TeamPlayer teamPlayer = teamPlayerRepository.findById(id).orElse(null)
        if(!teamPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team player found with ${id}".toString())
        }
        return teamPlayer
    }

    //Get Method - Caching
    TeamPlayer getTeamPlayerByIdCaching(int id){
        TeamPlayer teamPlayer = (TeamPlayer) clientConfig.getValue(id.toString(), 'teamPlayerId'){
            log.info("Inside fetcher closure for team player ${id}")
            TeamPlayer result = teamPlayerRepository.findById(id).orElse(null)
            log.info("Repository returned")
            return result
        }
        if(!teamPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No team player found with ${id}".toString())
        }
        return teamPlayer
    }

    List<ResponseTeamPlayerDTO> getPlayersByMatchId(int id){
        List<TeamPlayer> teamPlayers = teamPlayerRepository.findByMatch_Id(id)
        if(!teamPlayers){
//            throw new HttpStatusException(HttpStatus.NOT_FOUND,
//                    "No players found for the given match id ${id}".toString())
            return []
        }
        return teamPlayers.collect {new ResponseTeamPlayerDTO(it)}
    }

    TeamPlayer validateTeamPlayer(CreateTeamPlayerDTO teamPlayerDTO){
        Team team = teamRepository.findByName(teamPlayerDTO.team).orElse(null)
        Match match = matchRepository.findById(teamPlayerDTO.matchId).orElse(null)
        Player player = playerRepository.findByName(teamPlayerDTO.player).orElse(null)

        if(!team || !player){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "Invalid team or player name")
        }

        if(!match){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "Invalid match id")
        }

        if(match.team1.id != team.id && match.team2.id != team.id){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "The selected team is not part of the specified match")
        }

        boolean playersAlreadyInMatch = teamPlayerRepository.findByTeam_NameAndMatch_Id(team.name, match.id)
                                        .any {it.player.id == player.id}

        if(playersAlreadyInMatch){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "Player is already assigned to this match")
        }

        if(teamPlayerDTO.isCaptain){
            boolean existingCaptain = teamPlayerRepository.findByTeam_NameAndMatch_Id(team.name, match.id)
                                        .any {it.isCaptain}
            if(existingCaptain){
                throw new HttpStatusException(HttpStatus.CONFLICT,
                        "This team already has a captain for this match")
            }
        }

        if(teamPlayerDTO.isWicketKeeper){
            boolean existingWicketKeeper = teamPlayerRepository.findByTeam_NameAndMatch_Id(team.name, match.id)
                                            .any {it.isWicketKeeper}
            if(existingWicketKeeper){
                throw new HttpStatusException(HttpStatus.CONFLICT,
                        "This team already has a wicket keeper for this match")
            }
        }

        TeamPlayer teamPlayer= new TeamPlayer(
                team,
                match,
                player,
                teamPlayerDTO.isCaptain,
                teamPlayerDTO.isWicketKeeper
        )
        return teamPlayer
    }

    TeamPlayer postTeamPlayerMethod(CreateTeamPlayerDTO teamPlayerDTO){
        try{
            TeamPlayer teamPlayer = validateTeamPlayer(teamPlayerDTO)
            return teamPlayerRepository.save(teamPlayer)
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inserting team player: ${e.message}".toString())
        }
    }

    TeamPlayer checkTeamPlayer(TeamPlayer existingTeamPlayer, TeamPlayer teamPlayer){
        boolean unchanged = existingTeamPlayer.team.id == teamPlayer.team.id &&
                existingTeamPlayer.match.id == teamPlayer.match.id &&
                existingTeamPlayer.player.id == teamPlayer.player.id &&
                existingTeamPlayer.isCaptain == teamPlayer.isCaptain &&
                existingTeamPlayer.isWicketKeeper == teamPlayer.isWicketKeeper

        if(unchanged){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "No changes made for the given id ${existingTeamPlayer.id}".toString())
        }

        existingTeamPlayer.team = teamPlayer.team
        existingTeamPlayer.match = teamPlayer.match
        existingTeamPlayer.player = teamPlayer.player
        existingTeamPlayer.isCaptain = teamPlayer.isCaptain
        existingTeamPlayer.isWicketKeeper = teamPlayer.isWicketKeeper

        return existingTeamPlayer
    }

    TeamPlayer putTeamPlayerMethod(int id, CreateTeamPlayerDTO teamPlayerDTO){
        try {
            TeamPlayer existingTeamPlayer = teamPlayerRepository.findById(id).orElse(null)
            if(!existingTeamPlayer){
                throw new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Team Player not found with id ${id}".toString())
            }
            TeamPlayer teamPlayer = validateTeamPlayer(teamPlayerDTO)
            TeamPlayer checkResult = checkTeamPlayer(existingTeamPlayer, teamPlayer)
            return teamPlayerRepository.update(checkResult)
        } catch (HttpStatusException e) {
            throw e
        } catch (Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating team player: ${e.message}".toString())
        }
    }

    //Put method - Caching
    TeamPlayer putTeamPlayerCaching(int id, CreateTeamPlayerDTO teamPlayerDTO){
        try{
            TeamPlayer existingPlayer = teamPlayerRepository.findById(id).orElse(null)
            if(!existingPlayer){
                throw new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Team Player not found with id ${id}".toString())
            }
            TeamPlayer teamPlayer = validateTeamPlayer(teamPlayerDTO)
            TeamPlayer checkResult = checkTeamPlayer(existingPlayer, teamPlayer)

            TeamPlayer updated = teamPlayerRepository.update(checkResult)
            clientConfig.putValue(id.toString(), 'teamPlayerId', updated)
            log.info("Update team player with id ${id}")
            return updated
        }  catch (HttpStatusException e) {
            throw e
        } catch (Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating team player: ${e.message}".toString())
        }
    }

    TeamPlayer deleteTeamPlayerMethod(int id){
        TeamPlayer existingTeamPlayer = teamPlayerRepository.findById(id).orElse(null)
        if(!existingTeamPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND,
                    "Team Player not found with id ${id}".toString())
        }

        try{
            teamPlayerRepository.deleteById(id)
            return existingTeamPlayer
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting team player: ${e.message}".toString())
        }
    }

    //Delete method - Caching
    TeamPlayer deleteTeamPlayerCaching(int id){
        TeamPlayer existingTeamPlayer = teamPlayerRepository.findById(id).orElse(null)
        if(!existingTeamPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND,
                    "Team Player not found with id ${id}".toString())
        }
        try{
            teamPlayerRepository.deleteById(id)
            clientConfig.evict(id.toString(), 'teamPlayerId')
            log.info("Deleted player with id ${id}")
            return existingTeamPlayer
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting team player: ${e.message}".toString())
        }
    }
}