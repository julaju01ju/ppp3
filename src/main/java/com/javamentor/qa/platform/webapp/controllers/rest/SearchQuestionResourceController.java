package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.search.SearchQuestionParam;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api("Rest Controller for Search Question")
public class SearchQuestionResourceController {

    private final SearchQuestionParam searchQuestionParam;
    private final QuestionDtoService questionDtoService;

    public SearchQuestionResourceController(SearchQuestionParam searchQuestionParam, QuestionDtoService questionDtoService) {
        this.searchQuestionParam = searchQuestionParam;
        this.questionDtoService = questionDtoService;
    }

    @GetMapping("/api/search")
    @ApiOperation("Поиск вопросов с пагинацией отсортированных по ID вопроса")
    public ResponseEntity<PageDto<QuestionViewDto>> getPageSearchQuestionsPaginationById(@RequestParam(value = "request") String request,
                                                                            @RequestParam("page") Integer page,
                                                                            @RequestParam(required = false, name = "items",
                                                                            defaultValue = "10") Integer itemsOnPage) {
        Map<String, Object> params = searchQuestionParam.getAllParam(request);
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", itemsOnPage);
        PageDto<QuestionViewDto> pageDto = questionDtoService.getPageDto("paginationSearchQuestionsSortedById", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}