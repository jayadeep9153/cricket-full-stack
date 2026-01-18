package com.service

import com.caching.HazelcastClientConfig
import com.cricketinfo.entity.*
import com.dto.Match.*
import com.dto.ScoreDTO.*
import com.repository.*
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton

@Singleton
@Slf4j
class MatchService {
    final MatchRepository matchRepository
    final TeamRepository teamRepository
    final PlayerRepository playerRepository
    final TeamPlayerService teamPlayerService
    final ScoreRepository scoreRepository
    final HazelcastClientConfig clientConfig

    MatchService(MatchRepository matchRepository, TeamRepository teamRepository,
                 PlayerRepository playerRepository, TeamPlayerService teamPlayerService,
                 ScoreRepository scoreRepository, HazelcastClientConfig clientConfig){
        this.matchRepository = matchRepository
        this.teamRepository = teamRepository
        this.playerRepository = playerRepository
        this.teamPlayerService = teamPlayerService
        this.scoreRepository = scoreRepository
        this.clientConfig = clientConfig
    }

    List<ResponseMatchDTO> getAllMatches(){
        return matchRepository.findAll().collect {new ResponseMatchDTO(it)}
    }

    ResponseMatchDTO getMatchById(int id){
        Match matchId = matchRepository.findById(id).orElse(null)
        if(!matchId){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No match found with ${id}".toString())
        }
        return new ResponseMatchDTO(matchId)
    }

    //Get method - Caching
    ResponseMatchDTO getMatchByIdCaching(int id){
        ResponseMatchDTO resultMatch = (ResponseMatchDTO) clientConfig.getValue(id.toString(), 'matchId'){
            log.info("Inside fetcher closure for match ${id}")
            Match matchId = matchRepository.findById(id).orElse(null)
            log.info("Repository returned")
            return new ResponseMatchDTO(matchId)
        }
        if(!resultMatch){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No match found with ${id}".toString())
        }
        return resultMatch
    }

    List<AllTeamScoresDTO> getAllTeamsScores(){
        return scoreRepository.findAllTeamsScores()
    }

    List<TeamScoreDTO> getScoresByMatchId(int matchId){
        return scoreRepository.findTeamScoreByMatchId(matchId)
    }

    List<AllTeamsWicketsDTO> getAllTeamsWickets(){
        return scoreRepository.findAllTeamsWickets()
    }

    List<TeamWicketsDTO> getWicketsByMatchId(int matchId){
        return scoreRepository.findTeamWicketsByMatch(matchId)
    }

    List<PlayerStatsDTO> getPlayerStatsByMatchId(int matchId){
        return scoreRepository.findPlayerStatsByMatch(matchId)
    }

    List<BowlerStatsDTO> getBowlerStatsByMatchId(int matchId){
        return scoreRepository.findBowlerStatsByMatch(matchId)
    }

    Match validateMatch(CreateMatchDTO matchDTO){
        Team team1 = teamRepository.findByName(matchDTO.team1Name).orElse(null)
        Team team2 = teamRepository.findByName(matchDTO.team2Name).orElse(null)

        if (!team1 || !team2) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "One or both teams do not exist")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "One or both teams do not exist"])
        }

        if (team1.id == team2.id) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Both the teams should not be the same")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "Both the teams should not be the same"])
        }

        Team tossWinner = teamRepository.findByName(matchDTO.tossWinner).orElse(null)

        if (!tossWinner || (tossWinner.id != team1.id && tossWinner.id != team2.id)) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Toss winner must be either team1 or team2")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "Toss winner must be either team1 or team2"])
        }

        Team matchWinner = teamRepository.findByName(matchDTO.matchWinner).orElse(null)

        if (!matchWinner || (matchWinner.id != team1.id && matchWinner.id != team2.id)) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Match winner must be either team1 or team2")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "Match winner must be either team1 or team2"])
        }

        Player potm = playerRepository.findByName(matchDTO.potm).orElse(null)

        if (!potm) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid player of the match name")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "Invalid player of the match name"])
        }

        List<Player> teamPlayer1 = teamPlayerService.getPlayerByTeamName(team1.name)
        List<Player> teamPlayer2 = teamPlayerService.getPlayerByTeamName(team2.name)

        boolean belongsToTeam = (teamPlayer1.any {it.id == potm.id}) || (teamPlayer2.any {it.id == potm.id})

        if(!belongsToTeam){
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Player of the match must belong to team1 or team2")
//            return HttpResponse.status(HttpStatus.BAD_REQUEST)
//                    .body([message: "Player of the match must belong to team1 or team2"])
        }

        Match match = new Match(
                team1,
                team2,
                matchDTO.date,
                tossWinner,
                matchDTO.tossDecision,
                matchWinner,
                potm
        )
        return match
    }

    Match postMatchMethod(CreateMatchDTO matchDTO){
        try{
            Match match = validateMatch(matchDTO)
            Match existingMatch = matchRepository.findAll().find {
                it.date == match.date &&
                        ((it.team1.id == match.team1.id && it.team2.id == match.team2.id) ||
                                (it.team1.id == match.team2.id && it.team2.id == match.team1.id))
            }

            if(existingMatch){
                throw new HttpStatusException(HttpStatus.CONFLICT,
                        "A match between these two teams on this date already exists")
            }

            return matchRepository.save(match)
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inserting match ${e.message}".toString())
        }
    }

    Match checkMatch(Match existingMatch, Match match){
        boolean unchanged = existingMatch.team1.id == match.team1.id &&
                existingMatch.team2.id == match.team2.id &&
                existingMatch.tossWinner.id == match.tossWinner.id &&
                existingMatch.matchWinner.id ==match.matchWinner.id &&
                existingMatch.potm.id == match.potm.id &&
                existingMatch.date == match.date &&
                existingMatch.tossDecision == match.tossDecision

        if(unchanged){
//            return HttpResponse.status(HttpStatus.CONFLICT)
//                    .body([message: "No changes made", match: existingMatch])
            throw new HttpStatusException(HttpStatus.CONFLICT, "No changes made for the given id ${existingMatch.id}".toString())
        }
        existingMatch.team1 = match.team1
        existingMatch.team2 = match.team2
        existingMatch.tossWinner = match.tossWinner
        existingMatch.matchWinner = match.matchWinner
        existingMatch.potm = match.potm
        existingMatch.date = match.date
        existingMatch.tossDecision = match.tossDecision

        return existingMatch
    }

    Match putMatchMethod(int id, CreateMatchDTO matchDTO){
        Match existingMatch = matchRepository.findById(id).orElse(null)
        if(!existingMatch){
            throw new HttpStatusException(HttpStatus.NOT_FOUND,
                    "Match not found with id ${id}".toString())
        }
        Match match = validateMatch(matchDTO)
        Match checkMatchResult = checkMatch(existingMatch, match)
        return matchRepository.update(checkMatchResult)
    }

    Match deleteMatchMethod(int id){
        Match existingMatch = matchRepository.findById(id).orElse(null)
        if(!existingMatch){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Match not found with id ${id}".toString())
        }

        try{
            matchRepository.deleteById(id)
            return existingMatch
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting match: ${e.message}".toString())
        }
    }
}