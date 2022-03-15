package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
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

    public AdminResourceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @DeleteMapping("delete/{userId}")
    @ApiOperation( value = "Удаление пользователя по userId, через изменение значения свойства isEnabled с true, на false")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Пользователь удален"),
            @ApiResponse(code = 404, message = "Пользователь с userId=* не найден"),
            @ApiResponse(code = 400, message = "Неверный формат введенного userId (необходимо ввести целое положительное число)")
    })
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") Long userId) {
        Optional<User> optionalUser = userService.getById(userId);

        if (optionalUser.isPresent()) {
            userService.disableUserByEmail(optionalUser.get().getEmail());
            return ResponseEntity.ok().body("Пользователь удален");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь с userId=" + userId + " не найден");
    }
}
