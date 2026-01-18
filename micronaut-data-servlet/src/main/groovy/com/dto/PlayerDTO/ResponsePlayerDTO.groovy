package com.dto.PlayerDTO

import com.cricketinfo.entity.Player
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class ResponsePlayerDTO {
    int id
    String name

    ResponsePlayerDTO(Player player){
        this.id = player.id
        this.name = player.name
    }
}
