package com.javamentor.qa.platform.webapp.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = JmApplication.class)
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@TestPropertySource(properties = "test/resources/application.properties")
public abstract class AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DataSet(value = "dataset/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    public void requestToAdminApiWithAdminRole() throws Exception {

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

    }

    @Test
    @DataSet(value = "dataset/authenticationResourceControllerTest/user.yml", disableConstraints = true)
    public void requestToUserApiWithUserRole() throws Exception {

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

    }


}
