package com.javamentor.qa.platform.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.security.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {JmApplication.class})
@AutoConfigureMockMvc
class AuthenticationResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getToken() {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

        try {
            mockMvc.perform(
                            post("/api/auth/token/")
                                    .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTokenWithoutAuthentication() {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        try {
            mockMvc.perform(
                            post("/api/auth/token/")
                                    .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.content().string("Bad credentials"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}