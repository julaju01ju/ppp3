package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ali Veliev 10.12.2021
 */

@RestController
@Api("Rest Contoller to get a Question by ID")
public class QuestionResourceController {


    @Autowired
    private QuestionDtoService questionDtoService;

    private final QuestionService questionService;

    public QuestionResourceController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/api/user/question/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {

        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }

    @GetMapping("/api/user/question/{id}")
    @ApiOperation("Возвращает вопрос и тэги относящиеся к этому вопросу, по ИД вопроса.")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") Long id) {

        return questionDtoService.getQuestionById(id).isEmpty() ?
            new ResponseEntity<>("Wrong Question ID!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(questionDtoService.getQuestionById(id), HttpStatus.OK);

    }

}
