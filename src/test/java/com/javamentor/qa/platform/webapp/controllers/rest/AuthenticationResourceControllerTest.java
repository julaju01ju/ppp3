package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.security.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void returnTokenWithAuthentication() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

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
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void returnTokenWithoutAuthentication() throws Exception {

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
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void returnTokenWithoutBodyRequest() throws Exception {

        mockMvc.perform(
                        post("/api/auth/token/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void requestToAdminApiWithAdminRole() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

        String ADMIN_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ADMIN_TOKEN = "Bearer " + ADMIN_TOKEN.substring(ADMIN_TOKEN.indexOf(":") + 2, ADMIN_TOKEN.length() - 2);

        mockMvc.perform(
                        get("/api/testadmin/")
                                .header(AUTHORIZATION, ADMIN_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string("API ADMIN TEST"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void requestToUserApiWithUserRole() throws Exception {

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
                        get("/api/testuser/")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string("API USER TEST"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void requestToAdminApiWithUserRole() throws Exception {

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
                        get("/api/testadmin/")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void requestToUserApiWithAdminRole() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("ADMIN");
        authenticationRequest.setUsername("admin@mail.ru");

        String ADMIN_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ADMIN_TOKEN = "Bearer " + ADMIN_TOKEN.substring(ADMIN_TOKEN.indexOf(":") + 2, ADMIN_TOKEN.length() - 2);

        mockMvc.perform(
                        get("/api/testuser/")
                                .header(AUTHORIZATION, ADMIN_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DataSet(value = "datasets/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    void requestToUserApiWithoutToken() throws Exception {

        mockMvc.perform(
                        get("/api/testuser/")
                                .header(AUTHORIZATION, ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }
}