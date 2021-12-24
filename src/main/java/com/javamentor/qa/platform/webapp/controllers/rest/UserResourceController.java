package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Ali Veliev 29.11.2021
 */

@RestController
@Api("Rest Contoller to get a User by ID")
public class UserResourceController {

    @Autowired
    private UserDtoService userDtoService;

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
}
