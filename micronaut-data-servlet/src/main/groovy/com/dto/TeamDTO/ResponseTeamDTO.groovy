package com.dto.TeamDTO

import com.cricketinfo.entity.Team
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class ResponseTeamDTO {
    int id
    String name

    ResponseTeamDTO(Team team){
        this.id = team.id
        this.name = team.name
    }
}
