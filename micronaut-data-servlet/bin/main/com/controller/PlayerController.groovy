package com.controller

import com.cricketinfo.entity.*
import com.dto.PlayerDTO.*
import com.service.*
import io.micronaut.http.*
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

import java.nio.file.Path

@Controller("/players")
@Validated
class PlayerController {
    final PlayerService playerService

    PlayerController(PlayerService playerService){
        this.playerService = playerService
    }

    @Get("/")
    List<Player> getAll(){
        return playerService.getAllPlayers()
    }

    @Get("/{id}")
    Player getById(@PathVariable int id){
        Player player = playerService.getPlayerById(id)
        return player
    }

    //Get - Caching
    @Get("/cache/{id}")
    Player getByIdCaching(@PathVariable int id){
        Player player = playerService.getPlayerByIdCaching(id)
        return player
    }

    @Get("/search")
    List<Player> searchPlayerByName(@QueryValue String name){
        List<Player> player = playerService.searchByPlayer(name)
        return player
    }

    @Post("/")
    HttpResponse<?> addPlayer(@Body @Valid CreatePlayerDTO createPlayerDTO){
        Player player = playerService.postPlayerMethod(createPlayerDTO)
        return HttpResponse.status(HttpStatus.CREATED).body(player)
    }

    @Put("/{id}")
    HttpResponse<?> updatePlayer(@PathVariable int id, @Body @Valid CreatePlayerDTO createPlayerDTO){
        Player existingPlayer = playerService.putPlayerMethod(id, createPlayerDTO)
        return HttpResponse.ok([message: "Player updated successfully", player:existingPlayer])
    }

    //Put Method Caching
    @Put("/cache/{id}")
    HttpResponse<?> updatePlayerByCaching(@PathVariable int id, @Body @Valid CreatePlayerDTO createPlayerDTO){
        Player existingPlayer = playerService.putPlayerMethodCaching(id, createPlayerDTO)
        return HttpResponse.ok([message: "Player updated successfully", player:existingPlayer])
    }

    @Delete("/{id}")
    HttpResponse<?> deletePlayer(@PathVariable int id){
        Player existingPlayer = playerService.deletePlayerMethod(id)
        return HttpResponse.ok([message: "Player deleted successfully", player: existingPlayer])
    }

    //Delete Method Caching
    @Delete("/cache/{id}")
    HttpResponse<?> deletePlayerByCaching(@PathVariable int id){
        Player existingPlayer = playerService.deletePlayerMethodByCaching(id)
        return HttpResponse.ok([message: "Player deleted successfully", player: existingPlayer])
    }
}