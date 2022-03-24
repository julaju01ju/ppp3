package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.search.SearchQuestionParam;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@Api("Rest Controller for Search Question")
public class SearchQuestionResourceController {

    private final SearchQuestionParam searchQuestionParam;
    private final QuestionDtoService questionDtoService;
    private final TagService tagService;

    public SearchQuestionResourceController(SearchQuestionParam searchQuestionParam, QuestionDtoService questionDtoService, TagService tagService) {
        this.searchQuestionParam = searchQuestionParam;
        this.questionDtoService = questionDtoService;
        this.tagService = tagService;
    }

    @GetMapping("/api/search")
    @ApiOperation("Поиск вопросов с пагинацией отсортированных по ID вопроса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров поиска и пагинации. Вопросы отсортированы по ID"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getPageSearchQuestionsPaginationById(@RequestParam(value = "request") String request,
                                                                            @RequestParam("page") Integer page,
                                                                            @RequestParam(required = false, name = "items",
                                                                            defaultValue = "10") Integer itemsOnPage,
                                                                            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
                                                                            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {
        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }
        Map<String, Object> params = searchQuestionParam.getAllParam(request);
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", itemsOnPage);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        PageDto<QuestionViewDto> pageDto = questionDtoService.getPageQuestionsWithTags("paginationSearchQuestionsSortedById", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}