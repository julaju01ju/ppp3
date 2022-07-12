package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.dto.AnswerDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.AnswerCreateDto;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnAnswerService;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
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
    private final CommentAnswerService commentAnswerService;
    private final CommentDtoService commentDtoService;

    @Autowired
    public AnswerResourceController(
            AnswerDtoService answerDtoService,
            AnswerService answerService,
            UserDtoService userDtoService,
            QuestionService questionService,
            VoteOnAnswerService voteOnAnswerService,
            ReputationService reputationService,
            AnswerConverter answerConverter,
            CommentAnswerService commentAnswerService,
            CommentDtoService commentDtoService) {
        this.answerDtoService = answerDtoService;
        this.answerService = answerService;
        this.userDtoService = userDtoService;
        this.questionService = questionService;
        this.voteOnAnswerService = voteOnAnswerService;
        this.reputationService = reputationService;
        this.answerConverter = answerConverter;
        this.commentAnswerService = commentAnswerService;
        this.commentDtoService = commentDtoService;
    }

    @GetMapping("/{questionId}/answer")
    @ApiOperation(
            value = "Возвращает ответы на вопрос с questionId=* в виде List<AnswerDto>, в отсортированном виде. " +
                    "Сначала идет ответ, который был помог, затем ответы в порядке убывания по полезности")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены ответы на вопрос с questionId=*"),
            @ApiResponse(code = 400, message = "Неверный формат введенного questionId (необходимо ввести целое положительное число)"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден, либо на вопрос с questionId=* пока еще никто не ответил")
    })
    public ResponseEntity<?> getAllAnswerByQuestionId(@PathVariable("questionId") Long id) {
        return new ResponseEntity<>(answerDtoService.getAllAnswersByQuestionId(id), HttpStatus.OK);

    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    @ApiOperation(value = "Удаление ответа с answerId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ответ с answerId=* удален"),
            @ApiResponse(code = 404, message = "Ответ с answerId=* не найден"),
            @ApiResponse(code = 400, message = "Формат введенного answerId является не верным")
    })
    public ResponseEntity<?> deleteAnswerById(@PathVariable Long answerId) {
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
        return new ResponseEntity<>("Такого answer не существует", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/answer/add")
    @ApiOperation(value = "Добавление ответа к вопросу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ответ добавлен"),
            @ApiResponse(code = 400, message = "Ошибка добавления ответа: на данный вопрос пользователь уже отвечал, " +
                    "либо формат введенного answerId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с answerId=* не найден")
    })
    public ResponseEntity<?> addAnswerByQuestionId(@Valid @RequestBody AnswerCreateDto answerCreateDto, @PathVariable("questionId") Long questionId) {

        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

    @PostMapping("/{questionId}/answer/{answerId}/comment")
    @ApiOperation(value = "Добавление комментария к ответу(answerId=*) на вопрос(questionId=*)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Комментарий успешно добавлен"),
            @ApiResponse(code = 404, message = "Не найден вопрос с questionId=*, " +
                                                "либо не найден ответ с answerId=* " +
                                                "либо не найден комментарий с сommentId=* " +
                                                "или же ответ с answerId=* не связан с вопросом под questionId=*."),
            @ApiResponse(code = 400, message = "Комментарий не может быть пустым.")
    })
    public ResponseEntity<?> addCommentToAnswerByQuestionIdAndAnswerId(@PathVariable("questionId") Long questionId,
                                                                       @PathVariable("answerId") Long answerId,
                                                                       @Valid @RequestBody Optional<String> text) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Answer> answer = answerService.getById(answerId);
        Optional<Question> question = questionService.getById(questionId);

        if (question.isEmpty()) {
            return new ResponseEntity<>("Не найден вопрос с ID = " + questionId + ".", HttpStatus.NOT_FOUND);
        }
        if (answer.isEmpty()) {
            return new ResponseEntity<>("Не найден ответ с ID = " + answerId + ".", HttpStatus.NOT_FOUND);
        }
        if (answerService.isAnswerExistInQuestion(answerId, questionId)) {
            return new ResponseEntity<>("Ответ с answerId=* не связан с вопросом под questionId=*.", HttpStatus.BAD_REQUEST);
        }
        if (text.isEmpty()) {
            return new ResponseEntity<>("Комментарий не может быть пустым.", HttpStatus.BAD_REQUEST);
        }

        CommentAnswer commentAnswer = new CommentAnswer();
        commentAnswer.setText(text.get());
        commentAnswer.setUser(sender);
        commentAnswer.setAnswer(answer.get());
        commentAnswerService.persist(commentAnswer);
        Long commentId = commentAnswer.getComment().getId();
        Optional<CommentDto> optComDto = commentDtoService.getCommentDtoByCommentId(commentId);

        return optComDto.isEmpty() ?
                new ResponseEntity<> ("Комментарий с ID = " + commentId + ", не найден.", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(optComDto, HttpStatus.OK);
    }
}