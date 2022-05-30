package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAdminResourceController extends AbstractControllerTest {

    @Autowired
    AnswerService answerService;

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/roles.yml",
            "dataset/adminResourceController/users.yml",
    }
    , disableConstraints = true, cleanBefore = true
    )
    public void deleteUserById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin1@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        delete("/api/admin/delete/101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/roles.yml",
            "dataset/adminResourceController/users.yml",
    }
            , disableConstraints = true, cleanBefore = true
    )
    public void deleteUserByIdNotFound() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        delete("/api/admin/delete/155")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/roles.yml",
            "dataset/adminResourceController/users.yml",
            "dataset/UserResourceController/reputations.yml",
    }
            , disableConstraints = true, cleanBefore = true
    )
    public void getUserByIdForbidden() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        get("/api/user/100")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        delete("/api/admin/delete/100")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/user/100")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/testRoleUserAccess/roles.yml",
            "dataset/adminResourceController/testRoleUserAccess/users.yml",
    }
            , disableConstraints = true, cleanBefore = true
    )
    public void testRoleUserAccess() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        delete("/api/admin/delete/100")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @DataSet(value = {
            "dataset/adminResourceController/roles.yml",
            "dataset/adminResourceController/users.yml",
            "dataset/adminResourceController/questions.yml",
            "dataset/adminResourceController/answers.yml",
    })
    public void getListOfDeletedAnswersByUser() throws Exception{
        String USER_TOKEN = getToken("admin@mail.ru","ADMIN");
            mockMvc.perform(get("/api/admin/answer/delete?userId=101")
                    .header(AUTHORIZATION, USER_TOKEN))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].id").value(100))
                    .andExpect(jsonPath("$.[0].userId").value(101))
                    .andExpect(jsonPath("$.[0].questionId").value(100))
                    .andExpect(jsonPath("$.[0].body").value("Some answer with id 100"))
                    .andExpect(jsonPath("$.[0].isDeleted").value(true))
                    .andExpect(jsonPath("$.[1].id").value(101))
                    .andExpect(jsonPath("$.[1].userId").value(101))
                    .andExpect(jsonPath("$.[1].questionId").value(100))
                    .andExpect(jsonPath("$.[1].body").value("Some answer with id 101"))
                    .andExpect(jsonPath("$.[1].isDeleted").value(true));
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/roles.yml",
            "dataset/adminResourceController/users.yml",
    })
    public void getListOfDeletedAnswersByUserNotExists() throws Exception{
        String USER_TOKEN = getToken("admin@mail.ru","ADMIN");
        mockMvc.perform(get("/api/admin/answer/delete?userId=1000")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[0].userId").doesNotExist());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/testDeleteAnswerById/roles.yml",
            "dataset/adminResourceController/testDeleteAnswerById/users.yml",
            "dataset/adminResourceController/testDeleteAnswerById/questions.yml",
            "dataset/adminResourceController/testDeleteAnswerById/answers.yml",

    }
            , disableConstraints = true, cleanBefore = true
    )
    public void deleteAnswerByIdNotFound() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin1@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        delete("/api/admin/answer/101/delete")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/testDeleteAnswerById/roles.yml",
            "dataset/adminResourceController/testDeleteAnswerById/users.yml",
            "dataset/adminResourceController/testDeleteAnswerById/questions.yml",
            "dataset/adminResourceController/testDeleteAnswerById/answers.yml",

    }
            , disableConstraints = true, cleanBefore = true
    )
    public void deleteAnswerByIdIsOk() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin1@mail.ru");


        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        Optional<Answer> answer = answerService.getById(102L);

        //есть в базе, isDeleted - false
        Assertions.assertTrue(answer.isPresent());
        Assertions.assertFalse(answer.get().getIsDeleted());


        mockMvc.perform(
                        delete("/api/admin/answer/102/delete")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
                //ответ с id 102 удален

        //остается в базе, поле isDeleted - true
        answer = answerService.getById(102L);
        Assertions.assertTrue(answer.isPresent());
        Assertions.assertTrue(answer.get().getIsDeleted());
    }

    @Test
    @DataSet(value = {
            "dataset/adminResourceController/testDeleteAnswerById/roles.yml",
            "dataset/adminResourceController/testDeleteAnswerById/users.yml",
            "dataset/adminResourceController/testDeleteAnswerById/questions.yml",
            "dataset/adminResourceController/testDeleteAnswerById/answers.yml",

    }
            , disableConstraints = true, cleanBefore = true
    )
    public void deleteAnswerByIdIsForbidden() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = getToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        delete("/api/admin/answer/102/delete")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
