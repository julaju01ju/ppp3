package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.converters.QuestionConverter;
import com.javamentor.qa.platform.models.converters.TagConverter;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api("Rest Contoller for Question")
public class QuestionResourceController {

    private final TagService tagService;
    private final QuestionService questionService;
    private final QuestionConverter questionConverter;
    private final TagConverter tagConverter;

    public QuestionResourceController(TagService tagService, QuestionService questionService, QuestionConverter questionConverter, TagConverter tagConverter) {
        this.tagService = tagService;
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
    }

    @PostMapping("/api/user/question/")
    @ApiOperation("API создания вопроса. Получает объект QuestionCreateDto. " +
            "Возвращет объект QuestionDto. Поля Title, Description, Tag должны быть заполнены." +
            "Если хотя бы одно поле не заполнено возвращается HttpStatus.BAD_REQUEST." +
            "Проверяет есть ли присланный Tag в базе. Если нет - создает.")
    @Transactional
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {

        List<Tag> listTag = tagConverter.listTagDtoToListTag(questionCreateDto.getTags());
        listTag = tagService.getListTagForCreateQuestion(listTag);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setUser((User) authentication.getPrincipal());
        question.setDescription(questionCreateDto.getDescription());
        question.setTags(listTag);

        questionService.persist(question);

        return new ResponseEntity<>(questionConverter.questionToQuestionDto(question), HttpStatus.OK);
    }
}
