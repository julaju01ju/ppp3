package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
@RestController
@RequestMapping("/api/user/question")
@Api("Question Api")
public class CommentResourceController {

    private QuestionService questionService;
    private CommentQuestionService commentQuestionService;
    private CommentDtoService commentDtoService;

    @Autowired
    public CommentResourceController(QuestionService questionService,
                                      CommentQuestionService commentQuestionService,
                                      CommentDtoService commentDtoService) {

        this.questionService = questionService;
        this.commentQuestionService = commentQuestionService;
        this.commentDtoService = commentDtoService;
    }


    @PostMapping("/{questionId}/comment")
    @ApiOperation("Добавление комментария в вопрос по questionId=*, далее посредством запроса в б/д возвращает" +
            "данный комментарий как CommentDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Комментарий успешно добавлен в вопрос."),
            @ApiResponse(code = 404, message = "Вопрос с данным questionId=* не найден." +
                    "Либо комментарий с commentId=* не найден."),
            @ApiResponse(code = 400, message = "Пустой комментарий.")
    })
    public ResponseEntity<?> addCommentByQuestionId(@PathVariable("questionId") Long questionId,
                                                    @Valid @RequestBody Optional<String> text) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(questionId);

        if (question.isEmpty()) {
            return new ResponseEntity<>("Вопрос с данным ID = " + questionId + ", не найден.", HttpStatus.NOT_FOUND);
        }

        if (text.isEmpty()) {
            return new ResponseEntity<>("Комментарий не может быть пустым.", HttpStatus.BAD_REQUEST);
        }

        CommentQuestion commentQuestion = new CommentQuestion();
        commentQuestion.setQuestion(question.get());
        commentQuestion.setText(text.get());
        commentQuestion.setUser(sender);
        commentQuestionService.persist(commentQuestion);
        Long commentId = commentQuestion.getComment().getId();
        Optional<CommentDto> optComDto = commentDtoService.getCommentDtoByCommentId(commentId);

        return optComDto.isEmpty() ?
                new ResponseEntity<>("Комментарий с ID = " + commentId + ", не найден.", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(optComDto, HttpStatus.OK);
    }
}
