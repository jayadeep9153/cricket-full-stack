package com.service

import com.dto.ScoreDTO.ResponseScoreDTO
import com.repository.ScoreRepository
import groovy.util.logging.Slf4j
import jakarta.inject.Singleton

@Singleton
@Slf4j
class ScoreService {
    final ScoreRepository scoreRepository

    ScoreService(ScoreRepository scoreRepository){
        this.scoreRepository = scoreRepository
    }

    List<ResponseScoreDTO> getAllScores(){
        return scoreRepository.findAll().collect {new ResponseScoreDTO(it)}
    }
}
