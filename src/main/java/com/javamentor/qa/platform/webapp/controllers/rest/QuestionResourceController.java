package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@RestController
@Api("Rest Contoller to get a Question by ID")
public class QuestionResourceController {

    @Autowired
    private QuestionDtoService questionDtoService;

    @GetMapping("/api/user/question/{id}")
    @ApiOperation("Получение вопроса по ID")
    public Optional<QuestionDto> getQuestionById(@PathVariable("id") Long id) {

        return questionDtoService.getQuestionById(id);

    }

}