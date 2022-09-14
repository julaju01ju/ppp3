package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageResourceController extends AbstractControllerTest {

    @Test
    @DataSet(value = {
            "dataset/MessageResourceController/message_star.yml",
            "dataset/MessageResourceController/users.yml",
            "dataset/MessageResourceController/role.yml",

    }, disableConstraints = true, cleanBefore = true)
    public void testDeleteMessageStarById() throws Exception{

        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                delete("/api/user/message/star")
                        .header(AUTHORIZATION,USER_TOKEN)
                        .param("id", "101"))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals("0",
                entityManager.createNativeQuery("select count(id) from message_star where id = 1")
                        .getSingleResult().toString());

        mockMvc.perform(
                        delete("/api/user/message/star")
                                .header(AUTHORIZATION,USER_TOKEN)
                                .param("id", "103"))
                .andDo(print())
                .andExpect(status().isBadRequest());


        mockMvc.perform(
                        delete("/api/user/message/star")
                                .header(AUTHORIZATION,USER_TOKEN)
                                .param("id", "1000"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DataSet(value = {
            "dataset/MessageResourceController/role.yml",
            "dataset/MessageResourceController/AddMessageStar/chat.yml",
            "dataset/MessageResourceController/AddMessageStar/groupChat.yml",
            "dataset/MessageResourceController/users.yml",
            "dataset/MessageResourceController/AddMessageStar/groupChatHasUsers.yml",
            "dataset/MessageResourceController/AddMessageStar/message.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void addMessageStarByMessageId() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                        post("/api/user/message/101/star/")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());


//      пытаюсь добавить собщение из чата в котором юзер не состоит
        mockMvc.perform(
                        post("/api/user/message/104/star")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
////      добавляю в избранные еще три сообщения (к одному, которое уже добавлено), третье сообщение не должно добавляться
        mockMvc.perform(
                        post("/api/user/message/102/star")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/api/user/message/103/star")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/api/user/message/105/star")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
//// добавляю несуществующее сообщение
        mockMvc.perform(
                        post("/api/user/message/1000/star")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound()
                );
    }


}
