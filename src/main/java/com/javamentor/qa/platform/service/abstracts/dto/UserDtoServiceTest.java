package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDtoTest;

import java.util.List;

public interface UserDtoServiceTest extends PageDtoService<UserDtoTest> {
    List<UserDtoTest> getAllUsers();
}
