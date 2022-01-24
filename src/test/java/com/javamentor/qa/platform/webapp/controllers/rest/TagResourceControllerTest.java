package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TagResourceControllerTest
        extends AbstractControllerTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/trackedTag.yml",
            "dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/tag.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getAllTrackedTags() throws Exception {
        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        get("/api/user/tag/tracked")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("tagname1"))
                .andExpect(jsonPath("$[0].description").value("description1"));
    }

    @Test
    @DataSet(value = {"dataset/TagResourceController/users.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/TagResourceController/GetAllTagsOrderByNamePagination/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllTagsOrderByNamePaginationWithoutPageParam() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/GetAllTagsOrderByNamePagination/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllTagsOrderByNamePaginationWithoutItemParam() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(12))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].name").value("tagname1"))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }

    @Test
    @DataSet(value = {"dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/GetAllTagsOrderByNamePagination/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void getAllTagsOrderByNamePaginationWithPage2Items1() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name?page=2&items=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(12))
                .andExpect(jsonPath("$.totalResultCount").value(12))
                .andExpect(jsonPath("$.itemsOnPage").value(1));
    }


    @Test
    @DataSet(value = {
            "dataset/TagResourceController/ignoredTag.yml",
            "dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/tag.yml",
    }, disableConstraints = true, cleanBefore = true, transactional = false)
    public void getAllIgnoredTags() throws Exception {
        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        get("/api/user/tag/ignored")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].name").value("tagname3"))
                .andExpect(jsonPath("$[0].description").value("description3"));
    }

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/trackedTag.yml",
            "dataset/TagResourceController/ignoredTag.yml",
            "dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/tag.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addTrackedTag() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        post("/api/user/tag/100/tracked")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("tagname1"))
                .andExpect(jsonPath("$.description").value("description1"));

        String sql = "select tt.trackedTag.id from TrackedTag tt where tt.id = 100";
        List<Long> ttId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(ttId.size() == 1);

        sql = "select it.ignoredTag.id from IgnoredTag it where it.id = 100";
        List<Long> itId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(itId.size() == 0);
    }

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/ignoredTag.yml",
            "dataset/TagResourceController/trackedTag.yml",
            "dataset/TagResourceController/tag.yml",
            "dataset/TagResourceController/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addTrackedTagNotFound() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        post("/api/user/tag/200/tracked")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());

        String sql = "select tt.trackedTag.id from TrackedTag tt where tt.id = 102";
        List<Long> ttId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(ttId.size() == 0);

        sql = "select it.ignoredTag.id from IgnoredTag it where it.id = 102";
        List<Long> itId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(itId.size() == 1);
    }

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/ignoredTag.yml",
            "dataset/TagResourceController/trackedTag.yml",
            "dataset/TagResourceController/users.yml",
            "dataset/TagResourceController/tag.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addIgnoredTag() throws Exception {
        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        post("/api/user/tag/102/ignored")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(102))
                .andExpect(jsonPath("$.name").value("tagname3"))
                .andExpect(jsonPath("$.description").value("description3"));

        String sql = "select it.ignoredTag.id from IgnoredTag it where it.id = 102";
        List<Long> itId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(itId.size() == 1);

        sql = "select tt.trackedTag.id from TrackedTag tt where tt.id = 102";
        List<Long> ttId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(ttId.size() == 0);
    }

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/ignoredTag.yml",
            "dataset/TagResourceController/trackedTag.yml",
            "dataset/TagResourceController/tag.yml",
            "dataset/TagResourceController/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addIgnoredTagNotFound() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        post("/api/user/tag/200/ignored")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());

        String sql = "select it.ignoredTag.id from IgnoredTag it where it.id = 100";
        List<Long> itId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(itId.size() == 0);

        sql = "select tt.trackedTag.id from TrackedTag tt where tt.id = 100";
        List<Long> ttId = entityManager.createQuery(sql).getResultList();
        Assertions.assertTrue(ttId.size() == 1);
    }
}
