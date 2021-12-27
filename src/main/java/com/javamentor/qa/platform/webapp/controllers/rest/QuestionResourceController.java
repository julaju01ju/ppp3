package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api("Rest Contoller for Question")
@RequestMapping("/api/user/question")
public class QuestionResourceController {

    private final QuestionService questionService;
    private final QuestionConverter questionConverter;
    private final TagConverter tagConverter;

    public QuestionResourceController(QuestionService questionService, QuestionConverter questionConverter, TagConverter tagConverter) {
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
    }

    @PostMapping("/")
    @ApiOperation("API создания вопроса. Получает объект QuestionCreateDto. " +
            "Возвращет объект QuestionDto. Поля Title, Description, Tag должны быть заполнены." +
            "Если хотя бы одно поле не заполнено возвращается HttpStatus.BAD_REQUEST." +
            "Проверяет есть ли присланный Tag в базе. Если нет - создает.")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setUser((User) authentication.getPrincipal());
        question.setDescription(questionCreateDto.getDescription());
        question.setTags(tagConverter.listTagDtoToListTag(questionCreateDto.getTags()));

        questionService.persist(question);

        return new ResponseEntity<>(questionConverter.questionToQuestionDto(question), HttpStatus.OK);
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {

        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }
}
