package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private ReputationService reputationService;
    private QuestionService questionService;
    private VoteOnQuestionService voteOnQuestionService;

    @Autowired
    public QuestionResourceController(ReputationService reputationService, QuestionService questionService, VoteOnQuestionService voteOnQuestionService) {
        this.reputationService = reputationService;
        this.questionService = questionService;
        this.voteOnQuestionService = voteOnQuestionService;
    }


    @PostMapping("/{questionId}/upVote")
    @ApiOperation("запись в БД голосования за вопрос со значением UP")
    public ResponseEntity<?> insertUpVote(@PathVariable("questionId") Long questionId) {

        Question question = questionService.getById(questionId).get();

        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId(), VoteType.UP_VOTE))) {

            reputationService.increaseReputationByQuestionVoteUp(question, sender);

            voteOnQuestionService.insertUpVoteQuestion(question, sender);
        }
        return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("запись в БД голосования за вопрос со значением DOWN")
    public ResponseEntity<?> insertDownVote(@PathVariable("questionId") Long questionId) {

        Question question = questionService.getById(questionId).get();

        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId(), VoteType.DOWN_VOTE))) {

            reputationService.decreaseReputationByQuestionVoteDown(question, sender);

            voteOnQuestionService.insertDownVoteQuestion(question, sender);
        }
        return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
}}
