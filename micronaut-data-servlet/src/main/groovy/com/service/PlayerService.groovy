package com.service

import com.caching.HazelcastClientConfig
import com.cricketinfo.entity.Player
import com.dto.PlayerDTO.CreatePlayerDTO
import com.repository.PlayerRepository
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton

@Singleton
@Slf4j
class PlayerService {
    final PlayerRepository playerRepository
    final HazelcastClientConfig clientConfig

    PlayerService(PlayerRepository playerRepository, HazelcastClientConfig clientConfig){
        this.playerRepository = playerRepository
        this.clientConfig = clientConfig
    }

    List<Player> getAllPlayers(){
        return playerRepository.findAll()
    }

    Player getPlayerById(int id){
        Player player = playerRepository.findById(id).orElse(null)
        if(!player){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No player found with ${id}".toString())
        }
        return player
    }

    //Get Method Caching
    Player getPlayerByIdCaching(int id){
        Player player = (Player) clientConfig.getValue(id.toString(), 'playerId'){
            log.info("Inside fetcher closure for player ${id}")
            Player result = playerRepository.findById(id).orElse(null)
            log.info("Repository returned")
            return result
        }
        if(!player){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No player found with ${id}".toString())
        }
        return player
    }

    List<Player> searchByPlayer(String name){
        List<Player> player = playerRepository.findByNameLike("%${name}%")
        if(!player){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "No players found with name ${name}".toString())
        }
        return player
    }

    Player postPlayerMethod(CreatePlayerDTO createPlayerDTO){
        try{
            if(playerRepository.existsByName(createPlayerDTO.name)){
                throw new HttpStatusException(HttpStatus.CONFLICT, "Player ${createPlayerDTO.name} already exists".toString())
            }
            Player player = new Player(createPlayerDTO.name, createPlayerDTO.age, createPlayerDTO.role)
            return playerRepository.save(player)
        } catch(HttpStatusException e){
            throw e
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inserting player: ${e.message}".toString())
        }
    }

    Player putPlayerMethod(int id, CreatePlayerDTO createPlayerDTO) {
        Player existingPlayer = playerRepository.findById(id).orElse(null)
        if (!existingPlayer) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND,
                    "Player with id ${id} not found".toString())
        }

        if(createPlayerDTO.name == existingPlayer.name &&
                createPlayerDTO.age == existingPlayer.age &&
                createPlayerDTO.role == existingPlayer.role){
            throw new HttpStatusException(HttpStatus.CONFLICT, "No changes made")
        }

        if(playerRepository.existsByName(createPlayerDTO.name) && createPlayerDTO.name != existingPlayer.name){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "Player with name ${createPlayerDTO.name} already exists".toString())
        }

        existingPlayer.name = createPlayerDTO.name
        existingPlayer.age = createPlayerDTO.age
        existingPlayer.role = createPlayerDTO.role
        return playerRepository.update(existingPlayer)
    }

    //Put Method Caching
    Player putPlayerMethodCaching(int id, CreatePlayerDTO playerDTO){
        Player existingPlayer = playerRepository.findById(id).orElse(null)
        if(!existingPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND,
                    "Player with id ${id} not found".toString())
        }

        if(playerDTO.name == existingPlayer.name &&
                playerDTO.age == existingPlayer.age &&
                playerDTO.role == existingPlayer.role){
            throw new HttpStatusException(HttpStatus.CONFLICT, "No changes made")
        }

        if(playerRepository.existsByName(playerDTO.name) && playerDTO.name != existingPlayer.name){
            throw new HttpStatusException(HttpStatus.CONFLICT,
                    "Player with name ${playerDTO.name} already exists".toString())
        }

        existingPlayer.name = playerDTO.name
        existingPlayer.age = playerDTO.age
        existingPlayer.role = playerDTO.role
        clientConfig.putValue(id.toString(), 'playerId', existingPlayer)
        log.info("Update player with id ${id}")
        return playerRepository.update(existingPlayer)
    }

    Player deletePlayerMethod(int id){
        Player existingPlayer = playerRepository.findById(id).orElse(null)
        if(!existingPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Player with id ${id} not found".toString())
        }
        try{
            playerRepository.deleteById(id)
            return existingPlayer
        }  catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting player: ${e.message}".toString())
        }
    }

    //Delete Method Caching
    Player deletePlayerMethodByCaching(int id){
        Player existingPlayer = playerRepository.findById(id).orElse(null)
        if(!existingPlayer){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Player with id ${id} not found".toString())
        }
        try{
            playerRepository.deleteById(id)
            clientConfig.evict(id.toString(), 'playerId')
            log.info("Deleted player with id ${id}")
            return existingPlayer
        } catch(Exception e){
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting player: ${e.message}".toString())
        }
    }
}
