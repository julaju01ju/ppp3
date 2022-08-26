package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageResourceController extends AbstractControllerTest {

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
                                .param("id", "3"))
                .andDo(print())
                .andExpect(status().isBadRequest());


        mockMvc.perform(
                        delete("/api/user/message/star")
                                .header(AUTHORIZATION,USER_TOKEN)
                                .param("id", "10"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


}
