package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithValidParameters() throws Exception {

        String USER_TOKEN = super.getToken("0@mail.com", "pass0");

        mockMvc.perform(
                get("/api/user/chat/group?page=1&items=10")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithValidParametersWithoutItemsCount() throws Exception {

        String USER_TOKEN = super.getToken("0@mail.com", "pass0");

        mockMvc.perform(
                        get("/api/user/chat/group?page=1")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithoutParameters() throws Exception {

        String USER_TOKEN = super.getToken("0@mail.com", "pass0");

        mockMvc.perform(
                        get("/api/user/chat/group")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
