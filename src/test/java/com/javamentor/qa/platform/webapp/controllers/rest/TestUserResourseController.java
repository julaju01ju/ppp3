package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 02.12.2021
 */


@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest(UserResourseController.class)
public class TestUserResourseController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDtoService service;

    @Test
    void getUserById() throws Exception {
        when(service.getUserById(anyLong())).thenReturn(new UserDto());

        mockMvc.perform(get("/api/user/{userId}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",equalTo(1)));

    }

}
