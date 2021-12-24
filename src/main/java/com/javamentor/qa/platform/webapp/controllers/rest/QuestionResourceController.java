package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Rest controller to get a question count from DB")
@RequestMapping("/api/user/question")
public class QuestionResourceController {

    private final QuestionService questionService;

    public QuestionResourceController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {

         return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }
}
