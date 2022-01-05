package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 02.12.2021
 */


@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestUserResourseController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"})
    void getUserById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.fullName").value("Constantin"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(101));
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"})
    void shouldNotGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/105"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}
