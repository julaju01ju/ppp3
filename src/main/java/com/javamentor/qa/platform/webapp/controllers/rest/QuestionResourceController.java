package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Ali Veliev 10.12.2021
 */

@RestController
@RequestMapping("/api/user/question")
@Api("Rest Controller for Question")
public class QuestionResourceController {

    @Autowired
    private QuestionDtoService questionDtoService;

    private final QuestionService questionService;
    private final QuestionConverter questionConverter;
    private final TagConverter tagConverter;

    public QuestionResourceController(QuestionService questionService, QuestionConverter questionConverter, TagConverter tagConverter) {
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {

        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @ApiOperation("Возвращает вопрос и тэги относящиеся к этому вопросу, по ИД вопроса.")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") Long id) {

        Optional<QuestionDto> questionDto = questionDtoService.getQuestionById(id);
        return questionDto.isEmpty()
                ? new ResponseEntity<>("Wrong Question ID!", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(questionDto, HttpStatus.OK);

    }

    @PostMapping("/")
    @ApiOperation("API создания вопроса. Получает объект QuestionCreateDto. " +
            "Возвращает объект QuestionDto. Поля Title, Description, Tag должны быть заполнены." +
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


    @GetMapping()
    @ApiOperation("Получение пагинации QuestionDto с тэгами. " +
            "В качестве параметров принимает page, items, список trackedTag и ignoredTag" +
            "page - обязательный параметр" +
            "items - не обязательный на фронте, по умолчанию на бэк 10" +
            "trackedTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых есть хотя бы один из переданных тэгов" +
            "ignoredTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых нет данных тэгов.")
    public ResponseEntity<PageDto<QuestionDto>> getQuestions(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);


        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsWithGivenTags" ,params), HttpStatus.OK);
    }
}