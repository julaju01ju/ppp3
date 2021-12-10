package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.impl.dto.UserDtoDaoImplTest;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoServiceTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDtoServiceImplTest implements UserDtoServiceTest {

    private final UserDtoDaoImplTest userDtoDaoTest;

    public UserDtoServiceImplTest(UserDtoDaoImplTest userDtoDaoTest) {
        this.userDtoDaoTest = userDtoDaoTest;
    }

    @Override
    @Transactional
    public List<UserDtoTest> getAllUsers() {
        return userDtoDaoTest.getAllUsers();
    }
}
