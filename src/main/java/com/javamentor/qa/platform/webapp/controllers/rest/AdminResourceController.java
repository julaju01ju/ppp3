package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.model.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AdminResourceController {

    private final UserServiceImpl userService;

    public AdminResourceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("api/admin/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.getById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsEnabled(false);
            userService.update(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id = " + id + " not found");
        }
    }

}
