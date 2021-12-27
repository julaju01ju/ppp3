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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestQuestionResourceController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/question/questionQuestionApi.yml", "dataset/question/user.yml"}, disableConstraints = true)
    public void getQuestionCount() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                get("/api/user/question/count")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("8"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionById() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description to 101"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.authorId").value(101))
                .andExpect(jsonPath("$.authorName").value("Constantin"))
                .andExpect(jsonPath("$.authorImage").value("link"))
                .andExpect(jsonPath("$.authorReputation").value(50))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andExpect(jsonPath("$.countValuable").value(2))
                .andExpect(jsonPath("$.countAnswer").value(1))

                .andExpect(jsonPath("$.listTagDto.[0].id").value(101))
                .andExpect(jsonPath("$.listTagDto.[0].name").value("Some name here"))
                .andExpect(jsonPath("$.listTagDto.[0].description").value("Some text here"));

    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void shouldNotGetQuestionById() throws Exception {
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


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/105")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

}
