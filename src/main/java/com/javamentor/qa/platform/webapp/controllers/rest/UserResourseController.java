package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.impl.dto.UserDtoServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ali Veliev 29.11.2021
 */

@RestController
public class UserResourseController {

    private final UserDtoServiceImpl userDtoService;

    public UserResourseController(UserDtoServiceImpl userDtoService) {
        this.userDtoService = userDtoService;
    }


    @GetMapping("/api/user/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId){
        return userDtoService.getUserById(userId);
    }


}
