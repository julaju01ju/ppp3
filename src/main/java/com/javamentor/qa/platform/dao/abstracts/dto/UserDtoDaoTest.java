package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.javamentor.qa.platform.models.entity.user.UserTest;

import java.util.List;

public interface UserDtoDaoTest {
    List<UserDtoTest> getAllUsers();
}
