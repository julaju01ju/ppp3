package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = JmApplication.class)
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@TestPropertySource(properties = "test/resources/application.properties")
public abstract class AbstractControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    private AuthenticationRequest setUserAuth(String userName, String password){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword(password);
        authenticationRequest.setUsername(userName);
        return authenticationRequest;
    }

    public String getToken(String userName, String password) throws Exception {
        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(this.setUserAuth(userName,password)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);
        return  USER_TOKEN;
    }

}
