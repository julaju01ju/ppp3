package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ali Veliev 29.11.2021
 */

@RestController
@Api("User Api")
public class UserResourceController {

    private final UserDtoService userDtoService;
    private final UserService userService;

    @Autowired
    public UserResourceController(UserDtoService userDtoService,
                                  UserService userService) {
        this.userDtoService = userDtoService;
        this.userService = userService;
    }

    @GetMapping("/api/user/{userId}")
    @ApiOperation("Получение пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь с userId=* получен"),
            @ApiResponse(code = 404, message = "Пользователь с userId=* не найден"),
            @ApiResponse(code = 400, message = "Неверный формат введенного userId")
    })
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {

        return userDtoService.getUserById(userId).isEmpty() ?
                new ResponseEntity<>("Пользователь с userId=" + userId + " не найден", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/api/user/new")
    @ApiOperation("Возращает всех пользователей как объект класса PageDto<UserDto> отсортированных " +
            "по дате регистрации с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все пользователи, отсортированные " +
                    "по дате регистрации с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<UserDto>> getAllUsersOrderByPersistDatePagination(@RequestParam(value = "page") Integer page,
                                                                                    @RequestParam(value = "items", required = false,
                                                                                            defaultValue = "10") Integer items,
                                                                                    @RequestParam(value = "filter", required = false,
                                                                                            defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("filter", filter);

        PageDto<UserDto> pageDto = userDtoService.getPageDto("paginationAllUsersSortingByPersistDate", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @GetMapping("/api/user/reputation")
    @ApiOperation("Возращает всех пользователей как объект класса PageDto<UserDto> отсортированных " +
            "по репутации с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все пользователи, отсортированные " +
                    "по репутации с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<UserDto>> getPageAllUserSortedByReputation(@RequestParam("page") Integer page,
                                                                             @RequestParam(required = false, name = "items",
                                                                                     defaultValue = "10") Integer itemsOnPage,
                                                                             @RequestParam(value = "filter", required = false,
                                                                                     defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", itemsOnPage);
        params.put("filter", filter);

        return new ResponseEntity<>(userDtoService.getPageDto(
                "paginationAllUsersSortedByReputation", params), HttpStatus.OK);
    }

    @GetMapping("api/user/vote")
    @ApiOperation("Возращает всех пользователей как объект класса PageDto<UserDto> отсортированных " +
            "по сумме голосов, полученных за ответы и вопросы с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все пользователи, отсортированные " +
                    "по сумме голосов, полученных за ответы и вопросы с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<UserDto>> getPageAllUsersSortedByVote(@RequestParam(value = "page") Integer currentPageNumber,
                                                                        @RequestParam(value = "items", required = false,
                                                                                defaultValue = "10") Integer itemsOnPage,
                                                                        @RequestParam(value = "filter", required = false,
                                                                                defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPageNumber);
        params.put("itemsOnPage", itemsOnPage);
        params.put("filter", filter);

        return new ResponseEntity<>(userDtoService.getPageDto(
                "paginationAllUsersSortedByVote", params), HttpStatus.OK);
    }

    @PutMapping("/api/{userId}/change/password")
    @ApiOperation("Смена пароля с шифрованием")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пароль успешно изменён"),
            @ApiResponse(code = 404, message = "Пользователь с userId=* не найден"),
            @ApiResponse(code = 400, message = "Вы можете менять только свой пароль. " + "Пароль должен состоять минимум из шести символов и " +
                    "содержать хотя бы одну маленькую латинскую букву, одну заглавню латинскую букву, " +
                    "одну цифру, один из спецсимволов: @#$%. Пароль не должен совпадать с ранее существующим")
    })
    public ResponseEntity<?> updatePasswordByEmail(@PathVariable("userId") long userId, @RequestBody String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getId().equals(userId)) {
            String currentPass = user.getPassword();

            String pat = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%]).{6,}";
            if (password.matches(pat) && !passwordEncoder.matches(password, currentPass)) {
                userService.updatePasswordByEmail(user.getEmail(), passwordEncoder.encode(password));

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);

                return new ResponseEntity<>("Пароль изменён", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Пароль должен состоять минимум из шести символов и" +
                        "содержать хотя бы одну маленькую латинскую букву, одну заглавню латинскую букву, " +
                        "одну цифру, один из спецсимволов: @#$%. Пароль не должен совпадать с ранее существующим", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Вы можете менять только свой пароль", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/user/profile/questions")
    @ApiOperation("Возвращает все вопросы, которые задавал авторизованный пользователь")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы, которые задавал авторизованный пользователь"),
            @ApiResponse(code = 500, message = "Страницы пока что не существует")
    })
    public ResponseEntity<List<UserProfileQuestionDto>> getAllUserQuestions() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<UserProfileQuestionDto> listAllUserQuestions = userDtoService.getAllQuestionsByUserId(userId);
        return new ResponseEntity<>(listAllUserQuestions, HttpStatus.OK);
    }
}

