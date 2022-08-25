package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DataSet(value = {
            "dataset/MessageResourceController/DeleteMessageStar/message_star.yml",
            "dataset/MessageResourceController/DeleteMessageStar/users.yml",
            "dataset/MessageResourceController/DeleteMessageStar/role.yml",

    }, disableConstraints = true, cleanBefore = true)
    public void testDeleteMessageStarById() throws Exception{

        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                delete("/api/user/message/star")
                        .header(AUTHORIZATION,USER_TOKEN)
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals("0",
                entityManager.createNativeQuery("select count(id) from message_star where id = 1")
                        .getSingleResult().toString());

        mockMvc.perform(
                        delete("/api/user/message/star")
                                .header(AUTHORIZATION,USER_TOKEN)
                                .param("id", "2"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


}
