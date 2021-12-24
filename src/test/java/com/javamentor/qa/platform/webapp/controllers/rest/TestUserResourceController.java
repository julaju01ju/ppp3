package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;

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
public class TestUserResourceController {

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
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/105")
                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllUsersOrderByPersistDatePaginationWithOutPageParam() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllUsersOrderByPersistDatePaginationWithOutItemsParam() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(3))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllUsersOrderByPersistDatePaginationWithPage2Items1() throws Exception {

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

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=2&items=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(23))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(1))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 1);
        Assertions.assertTrue((int) list.get(0).get("id") == 101);
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllUsersOrderByPersistDatePaginationWithPage1Items50() throws Exception {

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

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=50")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(50))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 23);
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllUsersOrderByPersistDatePaginationWithPage1Items5CheckSorting() throws Exception {

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

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(5))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue((int) list.get(0).get("id") == 100);
        Assertions.assertTrue((int) list.get(1).get("id") == 101);
        Assertions.assertTrue((int) list.get(2).get("id") == 102);
        Assertions.assertTrue((int) list.get(3).get("id") == 103);
        Assertions.assertTrue((int) list.get(4).get("id") == 104);
    }
}
