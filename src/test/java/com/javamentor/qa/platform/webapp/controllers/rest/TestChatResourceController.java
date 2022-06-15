package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/roles.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/user_entity.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/chats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/singleChats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDateWithValidParameter() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "user");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/chat/101/single/message?page=1&items=4")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(5))
                .andExpect(jsonPath("$.itemsOnPage").value(4));

    }

    @Test
    @DataSet(value = {"dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/roles.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/user_entity.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/chats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/singleChats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDateWithoutItemsParameter() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "user");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/chat/101/single/message?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(5))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }

    @Test
    @DataSet(value = {"dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/roles.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/user_entity.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/chats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/singleChats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDateWithoutMessages() throws Exception {

        String USER_TOKEN = super.getToken("user2@mail.ru", "user");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/chat/103/single/message?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(0))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }

    @Test
    @DataSet(value = {"dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/roles.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/user_entity.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/chats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/singleChats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDateWithoutPageParameter() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "user");

        mockMvc.perform(
                        get("/api/user/chat/101/single/message")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/roles.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/user_entity.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/chats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/singleChats.yml",
            "dataset/ChatResourceController/getAllMessageDtoInSingleChatSortedByPersistDate/messages.yml",

    },
            disableConstraints = true, cleanBefore = true)
    public void getAllMessageDtoInSingleChatSortedByPersistDateWithNonExistentChatId() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "user");

        mockMvc.perform(
                        get("/api/user/chat/115/single/message?page=1&items=4")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
