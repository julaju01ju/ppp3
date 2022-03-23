package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.search.SearchQuestion;
import com.javamentor.qa.platform.search.SearchQuestionParam;
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

    private final SearchQuestion searchQuestion;
    private final SearchQuestionParam searchQuestionParam;

    public SearchQuestionResourceController(SearchQuestion searchQuestion, SearchQuestionParam searchQuestionParam) {
        this.searchQuestion = searchQuestion;
        this.searchQuestionParam = searchQuestionParam;
    }

    @GetMapping("/api/search")
    @ApiOperation("Поиск вопросов")
    public ResponseEntity<List<QuestionViewDto>> getQuestion(@RequestParam(value = "request") String request) {

        Map<String, Object> param = searchQuestionParam.getAllParam(request);
        return new ResponseEntity<>(searchQuestion.getItems(param), HttpStatus.OK);
    }
}