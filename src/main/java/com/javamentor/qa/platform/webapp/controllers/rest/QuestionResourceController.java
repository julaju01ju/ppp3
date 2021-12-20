package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Maksim Solovev 16.12.2021.
 */

@RestController
@Api("Rest Contoller to vote for a question")
@RequestMapping("api/user/question")
public class QuestionResourceController {

    @Autowired
    private ReputationService reputationService;
    public QuestionResourceController(ReputationService reputationService){
        this.reputationService = reputationService;
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation("увеличение репутации автора вопроса на +10")
    public ResponseEntity<?> getQuestionReputation(@PathVariable("questionId") Long questionId) {

        return new ResponseEntity<>(reputationService.getReputationCount(questionId), HttpStatus.OK);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("снижение репутации автора вопроса на -5")
    public ResponseEntity<?> getQuestionReputation(@PathVariable("questionId") Long questionId) {
        Long questionReputation = reputationService.getReputationCount(questionId);

        return !reputationService ?
                new ResponseEntity<>("Question with id " + questionId + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(reputationService.getReputationCount(questionId), HttpStatus.OK);
    }
}
