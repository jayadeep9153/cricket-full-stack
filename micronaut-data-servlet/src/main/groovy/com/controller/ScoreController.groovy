package com.controller

import com.cricketinfo.entity.*
import com.dto.ScoreDTO.ResponseScoreDTO
import com.repository.*
import com.service.ScoreService
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated

@Validated
@Controller("/scores")
class ScoreController {
    final ScoreService scoreService

    ScoreController(ScoreService scoreService){
        this.scoreService = scoreService
    }

    @Get("/")
    List<ResponseScoreDTO> getAll(){
        return scoreService.getAllScores()
    }
}