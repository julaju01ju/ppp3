package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.javamentor.qa.platform.service.impl.dto.UserDtoServiceImplTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserControllerTest {
    private final UserDtoServiceImplTest userDtoServiceTest;

    @Autowired
    public UserControllerTest(UserDtoServiceImplTest userDtoServiceTest) {
        this.userDtoServiceTest = userDtoServiceTest;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDtoTest>> getAllUsers() {
        final List<UserDtoTest> allUsers = userDtoServiceTest.getAllUsers();
        return allUsers != null && !allUsers.isEmpty()
                ? new ResponseEntity<>(allUsers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/paginationExample")
    public ResponseEntity<PageDto<UserDtoTest>> getPageDtoExample(
                                                  @RequestParam("currentPageNumber") Integer currentPageNumber
                                                , @RequestParam("itemsOnPage") Integer itemsOnPage) {

        PageDto<UserDtoTest> pageDto;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPageNumber);
        params.put("itemsOnPage", itemsOnPage);
        pageDto = userDtoServiceTest.getPageDto("paginationUserTest", params);

        return pageDto != null && !pageDto.getItems().isEmpty()
                ? new ResponseEntity<>(pageDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
