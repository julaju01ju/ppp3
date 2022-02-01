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
@Api("Admin controller")
public class AdminResourceController {

    private final UserService userService;

    public AdminResourceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation( value = "Удаление пользователя по ID, через изменение значения свойства isEnabled с true, на false")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "User is deleted"),
            @ApiResponse(code = 404, message = "User with id not found")
    })
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.getById(id);

        if (optionalUser.isPresent()) {
            userService.disableUserByEmail(optionalUser.get().getEmail());
            return ResponseEntity.ok().body("User is deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id = " + id + " not found");
    }
}
