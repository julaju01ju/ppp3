package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.EMPTY;
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
    }

    @Test
    @DataSet(value = {
            "dataset/TagResourceController/ignoredTag.yml",
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

    }
}
