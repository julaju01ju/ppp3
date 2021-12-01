package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResourceAnswerConetoller {

    private final AnswerDtoService answerDtoService;

    @Autowired
    public ResourceAnswerConetoller(AnswerDtoService answerDtoService){
        this.answerDtoService = answerDtoService;
    }

    @GetMapping("api/user/question/{questionId}/answer")
    public ResponseEntity<List<AnswerDto>> getAllAnswerByQuestionId(@PathVariable("questionId") Long id){
        List<AnswerDto> answerDtos = answerDtoService.getAllByQuestionId(id);
        return new ResponseEntity<>(answerDtos, HttpStatus.OK);
    }

}
