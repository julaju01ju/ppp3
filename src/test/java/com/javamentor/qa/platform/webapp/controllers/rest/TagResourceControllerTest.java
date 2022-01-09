package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TagResourceControllerTest
        extends AbstractControllerTest {


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



}
