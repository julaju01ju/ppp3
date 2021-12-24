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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api("Rest controller to get a question count from DB")
@RequestMapping("/api/user/question")
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

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {

        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation("запись в БД голосования за вопрос со значением UP")
    public ResponseEntity<?> insertUpVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                reputationService.changeReputationByQuestion(question, sender, VoteType.UP_VOTE);
                voteOnQuestionService.insertUpVoteQuestion(question, sender);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Такого question не существует", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("запись в БД голосования за вопрос со значением DOWN")
    public ResponseEntity<?> insertDownVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                reputationService.changeReputationByQuestion(question, sender, VoteType.DOWN_VOTE);
                voteOnQuestionService.insertDownVoteQuestion(question, sender);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Такого question не существует", HttpStatus.NOT_FOUND);
    }
}

