package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/ChatResourceController/roles.yml",
            "dataset/ChatResourceController/user_entity.yml",
            "dataset/ChatResourceController/chats.yml",
            "dataset/ChatResourceController/singleChats.yml",
            "dataset/ChatResourceController/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDate() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "user");

        mockMvc.perform(
                        get("/api/user/chat/101/single/message?page=1&items=4")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/user/chat/101/single/message?page=1")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/user/chat/101/single/message")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        get("/api/user/chat/115/single/message?page=1&items=4")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
