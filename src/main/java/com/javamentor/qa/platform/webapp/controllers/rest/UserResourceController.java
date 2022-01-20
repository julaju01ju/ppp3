package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Ali Veliev 29.11.2021
 */

@RestController
@Api("Rest Contoller to get a User by ID")
public class UserResourceController {

    private UserDtoService userDtoService;
    private UserService userservice;

    @Autowired
    public UserResourceController(UserDtoService userDtoService, UserService userservice) {
        this.userDtoService = userDtoService;
        this.userservice = userservice;
    }

    @GetMapping("/api/user/{userId}")
    @ApiOperation("Получение пользователя по ID")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {

        return userDtoService.getUserById(userId).isEmpty() ?
                new ResponseEntity<>("User with id " + userId + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/api/user/new")
    @ApiOperation("API получение всех пользователей, отсортированных по дате регистрации, с пагинацией. " +
            "Принимает параметры: page(обязательный) - текущая страница и " +
            "items(необязательный) - количество элементов на страницу. По умолчанию равен 10.")
    public ResponseEntity<PageDto<UserDto>> getAllUsersOrderByPersistDatePagination(@RequestParam(value = "page") Integer page,
                                                                                    @RequestParam(value = "items", required = false,
                                                                                            defaultValue = "10") Integer items) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);

        PageDto<UserDto> pageDto = userDtoService.getPageDto("paginationAllUsersSortingByPersistDate", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @GetMapping("/api/user/reputation")
    @ApiOperation("Получение списка всех пользователей с пагинацией, отсортированных по репутации")
    public ResponseEntity<PageDto<UserDto>> getPageAllUserSortedByReputation(@RequestParam("page") Integer page
            , @RequestParam(required = false, name = "items", defaultValue = "10") Integer itemsOnPage) {

        Map<String, Object> params = new HashMap<>();

        params.put("currentPageNumber", page);
        params.put("itemsOnPage", itemsOnPage);

        return new ResponseEntity<>(userDtoService.getPageDto(
                "paginationAllUsersSortedByReputation", params), HttpStatus.OK);
    }

    @GetMapping("api/user/vote")
    @ApiOperation("Получение всех пользователей с пагинацией отсортированных по сумме голосов, полученных за ответы и вопросы")
    public ResponseEntity<PageDto<UserDto>> getPageAllUsersSortedByVote(@RequestParam(value = "page") Integer currentPageNumber,
                                                                        @RequestParam(value = "items", required = false,
                                                                                defaultValue = "10") Integer itemsOnPage) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPageNumber);
        params.put("itemsOnPage", itemsOnPage);

        return new ResponseEntity<>(userDtoService.getPageDto(
                "paginationAllUsersSortedByVote", params), HttpStatus.OK);
    }

    @GetMapping("/api/user/change/password")
    @ApiOperation("Смена пароля с шифрованием")
    public String updatePassword(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String currentPass = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        String pat = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%]).{6,}";
        if (user.getPassword().matches(pat) && !user.getPassword().equals(currentPass)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userservice.update(user);
            return "users";
        } else {
            return "invalidpass";
        }
    }
}
