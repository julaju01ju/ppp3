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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@TestPropertySource(properties = "test/resources/application.properties")
class AuthenticationResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "dataset/authenticationResourceControllerTest/user.yml",
            disableConstraints = true, cleanBefore = true)
    public void returnTokenWithAuthentication() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("adminAUTH@mail.ru");

        mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = "dataset/authenticationResourceControllerTest/user.yml",
            disableConstraints = true, cleanBefore = true)
    public void returnTokenWithoutAuthentication() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("Bad credentials"));
    }

    @Test
    @DataSet(value = "dataset/authenticationResourceControllerTest/user.yml",
            disableConstraints = true, cleanBefore = true)
    public void returnTokenWithoutBodyRequest() throws Exception {

        mockMvc.perform(
                        post("/api/auth/token/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}
