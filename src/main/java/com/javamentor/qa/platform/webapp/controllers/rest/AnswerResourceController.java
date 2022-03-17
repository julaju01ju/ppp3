package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerCreateDto;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnAnswerService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.DOWN_VOTE;
import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.UP_VOTE;


@RestController
@Api("Answer Api")
@RequestMapping("api/user/question")
public class AnswerResourceController {

    private final AnswerDtoService answerDtoService;
    private final AnswerService answerService;
    private final UserDtoService userDtoService;
    private final QuestionService questionService;
    private final VoteOnAnswerService voteOnAnswerService;
    private final ReputationService reputationService;
    private final AnswerConverter answerConverter;

    @Autowired
    public AnswerResourceController(
            AnswerDtoService answerDtoService,
            AnswerService answerService,
            UserDtoService userDtoService,
            QuestionService questionService,
            VoteOnAnswerService voteOnAnswerService,
            ReputationService reputationService,
            AnswerConverter answerConverter) {
        this.answerDtoService = answerDtoService;
        this.answerService = answerService;
        this.userDtoService = userDtoService;
        this.questionService = questionService;
        this.voteOnAnswerService = voteOnAnswerService;
        this.reputationService = reputationService;
        this.answerConverter = answerConverter;
    }

    @GetMapping("/{questionId}/answer")
    @ApiOperation(
            value = "Возвращает ответы на вопрос с questionId=* в виде List<AnswerDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены ответы на вопрос с questionId=*"),
            @ApiResponse(code = 400, message = "Неверный формат введенного questionId (необходимо ввести целое положительное число)"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден, либо на вопрос с questionId=* пока еще никто не ответил")
    })
    public ResponseEntity<?> getAllAnswerByQuestionId(@PathVariable("questionId") Long questionId) {
        if (questionService.existsById(questionId)) {
            List<AnswerDto> answerDtoList = answerDtoService.getAllAnswersByQuestionId(questionId);
            if (answerDtoList.isEmpty()) {
                return new ResponseEntity<>("На вопрос с questionId=" + questionId + " пока никто не ответил", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(answerDtoService.getAllAnswersByQuestionId(questionId), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Вопрос с questionId=" + questionId + " не найден");
        }
    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    @ApiOperation(value = "Удаление ответа с answerId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ответ с answerId=* удален"),
            @ApiResponse(code = 400, message = "Неверный формат введенного answerId (необходимо ввести целое положительное число)"),
            @ApiResponse(code = 404, message = "Ответ с answerId=* не найден")
    })
    public ResponseEntity<?> deleteAnswerById(@PathVariable Long answerId) {
        if (answerId == null) {
            return ResponseEntity.badRequest().body("Ошибка уделения ответа с answerId=" + answerId + " (поле answerId не заполнено)");
        }
        if (answerService.existsById(answerId)) {
            answerService.deleteById(answerId);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity("Ответ с answerId=" + answerId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/answer/{answerId}/upVote")
    @ApiOperation(value = "Запись в БД голосования со значением UP за ответ c answerId=* на вопрос с questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Поднятие репутации ответа с answerId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId/answerId является не верным"),
            @ApiResponse(code = 404, message = "Ответ с answerId=* не найден")
    })
    public ResponseEntity<?> insertUpVote(@PathVariable("questionId") Long questionId, @PathVariable("answerId") Long answerId) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!(voteOnAnswerService.getIfNotExists(answer.getId(), sender.getId()))) {
                VoteAnswer upVoteAnswer = new VoteAnswer(sender, answer, UP_VOTE);
                voteOnAnswerService.persist(upVoteAnswer);
                return new ResponseEntity<>(voteOnAnswerService.getCountOfVotes(answerId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Ответ с answerId=" + answerId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/answer/{answerId}/downVote")
    @ApiOperation(value = "Запись в БД голосования со значением DOWN за ответ c answerId=* на вопрос с questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Понижение репутации ответа с answerId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId/answerId является не верным"),
            @ApiResponse(code = 404, message = "Ответ с answerId=* не найден")
    })
    public ResponseEntity<?> insertDownVote(@PathVariable("questionId") Long questionId, @PathVariable("answerId") Long answerId) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!(voteOnAnswerService.getIfNotExists(answer.getId(), sender.getId()))) {
                VoteAnswer downVoteAnswer = new VoteAnswer(sender, answer, DOWN_VOTE);
                voteOnAnswerService.persist(downVoteAnswer);
                return new ResponseEntity<>(voteOnAnswerService.getCountOfVotes(answerId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Ответ с answerId=*" + answerId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/answer/add")
    @ApiOperation(value = "Добавление ответа к вопросу с questionId=*. В RequestBody ожидает объект AnswerCreateDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ответ к вопросу с questionId=* добавлен"),
            @ApiResponse(code = 400, message = "Ошибка добавления вопроса: на данный вопрос пользователь" +
                    " уже отвечал, неверный формат введенного questionId или объект AnswerCreateDto не передан в RequestBody"),
            @ApiResponse(code = 404, message = "Вопрос c questionId=* не найден")
    })
    public ResponseEntity<?> addAnswerByQuestionId(@Valid @RequestBody AnswerCreateDto answerCreateDto, @PathVariable("questionId") Long questionId) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (answerService.getIfNotExists(question.getId(), sender.getId())) {
                Answer answer = new Answer();
                answer.setHtmlBody(answerCreateDto.getBody());
                answer.setUser(sender);
                answer.setQuestion(question);
                answer.setIsDeleted(false);
                answer.setIsDeletedByModerator(false);
                answer.setIsHelpful(false);
                answer.setDateAcceptTime(LocalDateTime.now());
                answerService.persist(answer);
                return new ResponseEntity<>(answerConverter.answerToAnswerDto(answer), HttpStatus.OK);
            }
            return new ResponseEntity<>("Вы уже отвечали на данный вопрос", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Вопрос c questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
    }

}