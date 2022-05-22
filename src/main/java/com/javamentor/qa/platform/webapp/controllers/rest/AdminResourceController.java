package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.model.AnswerServiceImpl;
import com.javamentor.qa.platform.service.impl.model.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/admin/")
@Api("Admin Api")
public class AdminResourceController {

    private final UserService userService;
    public final AnswerService answerService;

    public AdminResourceController(UserServiceImpl userService, AnswerServiceImpl answerService) {
        this.userService = userService;
        this.answerService = answerService;
    }

    @DeleteMapping("delete/{userId}")
    @ApiOperation( value = "Удаление пользователя по userId, через изменение значения свойства isEnabled с true, на false")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Пользователь удален"),
            @ApiResponse(code = 404, message = "Пользователь с userId=* не найден"),
            @ApiResponse(code = 400, message = "Неверный формат введенного userId")
    })
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") Long userId) {
        Optional<User> optionalUser = userService.getById(userId);

        if (optionalUser.isPresent()) {
            userService.disableUserByEmail(optionalUser.get().getEmail());
            return ResponseEntity.ok().body("Пользователь удален");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь с userId=" + userId + " не найден");
    }

    @DeleteMapping("answer/{answerId}/delete")
    @ApiOperation( value = "Удаление ответа по userId, через изменение значения свойства isDeleted с false, на true")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Ответ удален"),
            @ApiResponse(code = 404, message = "Ответ с указанным id не найден"),
            @ApiResponse(code = 400, message = "Неверный формат введенного Id")
    })
    public ResponseEntity<?> deleteAnswerById(@PathVariable("answerId") Long answerId) {
        Optional<Answer> optionalAnswer = answerService.getById(answerId);

        if (optionalAnswer.isPresent()) {
            answerService.deleteById(optionalAnswer.get().getId());
            return ResponseEntity.ok().body("Ответ удален");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Вопрос с Id=" + answerId + " не найден");
    }
}
