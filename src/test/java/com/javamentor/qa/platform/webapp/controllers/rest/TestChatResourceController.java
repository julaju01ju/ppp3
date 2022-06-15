package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithValidParameters() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                get("/api/user/chat/group?page=1&items=10")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.chatName").value("Some group chat 1"))
                .andExpect(jsonPath("$.page.totalPageCount").value(1))
                .andExpect(jsonPath("$.page.itemsOnPage").value(10))
                .andExpect(jsonPath("$.page.items[0].id").value(101))
                .andExpect(jsonPath("$.page.items[0].message").value("Some message in group chat 1"))
                .andExpect(jsonPath("$.page.items[0].nickName").value("Constantin"))
                .andExpect(jsonPath("$.page.items[0].userId").value(101))
                .andExpect(jsonPath("$.page.items[0].image").value("link"))
                .andExpect(jsonPath("$.page.items[0].persistDateTime").value("2022-06-14T03:00:00"))
                .andExpect(jsonPath("$.page.items[1].id").value(102))
                .andExpect(jsonPath("$.page.items[1].message").value("Some message in group chat 2"))
                .andExpect(jsonPath("$.page.items[1].nickName").value("Nicolay"))
                .andExpect(jsonPath("$.page.items[1].userId").value(102))
                .andExpect(jsonPath("$.page.items[1].image").value("link"))
                .andExpect(jsonPath("$.page.items[1].persistDateTime").value("2022-06-14T03:00:00"))
                .andExpect(jsonPath("$.page.items[2].id").value(103))
                .andExpect(jsonPath("$.page.items[2].message").value("Some message in group chat 3"))
                .andExpect(jsonPath("$.page.items[2].nickName").value("Petya"))
                .andExpect(jsonPath("$.page.items[2].userId").value(103))
                .andExpect(jsonPath("$.page.items[2].image").value("link"))
                .andExpect(jsonPath("$.page.items[2].persistDateTime").value("2022-06-14T03:00:00"));

    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithValidParametersWithoutItemsCount() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                        get("/api/user/chat/group?page=1")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.chatName").value("Some group chat 1"))
                .andExpect(jsonPath("$.page.totalPageCount").value(1))
                .andExpect(jsonPath("$.page.itemsOnPage").value(10))
                .andExpect(jsonPath("$.page.items[0].id").value(101))
                .andExpect(jsonPath("$.page.items[0].message").value("Some message in group chat 1"))
                .andExpect(jsonPath("$.page.items[0].nickName").value("Constantin"))
                .andExpect(jsonPath("$.page.items[0].userId").value(101))
                .andExpect(jsonPath("$.page.items[0].image").value("link"))
                .andExpect(jsonPath("$.page.items[0].persistDateTime").value("2022-06-14T03:00:00"))
                .andExpect(jsonPath("$.page.items[1].id").value(102))
                .andExpect(jsonPath("$.page.items[1].message").value("Some message in group chat 2"))
                .andExpect(jsonPath("$.page.items[1].nickName").value("Nicolay"))
                .andExpect(jsonPath("$.page.items[1].userId").value(102))
                .andExpect(jsonPath("$.page.items[1].image").value("link"))
                .andExpect(jsonPath("$.page.items[1].persistDateTime").value("2022-06-14T03:00:00"))
                .andExpect(jsonPath("$.page.items[2].id").value(103))
                .andExpect(jsonPath("$.page.items[2].message").value("Some message in group chat 3"))
                .andExpect(jsonPath("$.page.items[2].nickName").value("Petya"))
                .andExpect(jsonPath("$.page.items[2].userId").value(103))
                .andExpect(jsonPath("$.page.items[2].image").value("link"))
                .andExpect(jsonPath("$.page.items[2].persistDateTime").value("2022-06-14T03:00:00"));;

    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void getGroupChatWithoutParameters() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                        get("/api/user/chat/group")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
