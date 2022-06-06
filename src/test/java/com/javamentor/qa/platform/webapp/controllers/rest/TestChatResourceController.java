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
            "dataset/ChatResourceController/role.yml",
            "dataset/ChatResourceController/users.yml",
            "dataset/ChatResourceController/singleChats.yml",
            "dataset/ChatResourceController/messages.yml",

    },
            disableConstraints = true, cleanBefore = true
    )

    public void getSingleChatDtosByUser() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        get("/api/user/chat/single?page=1&items=15")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(6))
                .andExpect(jsonPath("$.items[0].id").value(105))
                .andExpect(jsonPath("$.items[0].name").value("NickName6"))
//                Изменить дату для своего часового пояса

                .andExpect(jsonPath("$.items[0].persistDateTimeLastMessage")
                        .value("2021-12-24T09:00:00"))
                .andExpect(jsonPath("$.items[0].lastMessage").value("Hello12"))
                .andExpect(jsonPath("$.items[0].image").value("link106"))

                .andExpect(jsonPath("$.items[1].id").value(106))
                .andExpect(jsonPath("$.items[1].name").value("NickName107"))
                .andExpect(jsonPath("$.items[1].persistDateTimeLastMessage")
                        .value("2021-12-12T09:00:00"))
                .andExpect(jsonPath("$.items[1].lastMessage").value("LastOne"))
                .andExpect(jsonPath("$.items[1].image").value("link107"))

                .andExpect(jsonPath("$.items[2].id").value(104))
                .andExpect(jsonPath("$.items[2].name").value("NickName105"))
                .andExpect(jsonPath("$.items[2].persistDateTimeLastMessage")
                        .value("2021-12-04T09:00:00"))
                .andExpect(jsonPath("$.items[2].lastMessage").value("Hello104"))
                .andExpect(jsonPath("$.items[2].image").value("link105"))

                .andExpect(jsonPath("$.items[3].id").value(103))
                .andExpect(jsonPath("$.items[3].name").value("NickName104"))
                .andExpect(jsonPath("$.items[3].persistDateTimeLastMessage")
                        .value("2021-12-03T09:00:00"))
                .andExpect(jsonPath("$.items[3].lastMessage").value("Hello103"))
                .andExpect(jsonPath("$.items[3].image").value("link104"))

                .andExpect(jsonPath("$.items[4].id").value(102))
                .andExpect(jsonPath("$.items[4].name").value("NickName103"))
                .andExpect(jsonPath("$.items[4].persistDateTimeLastMessage")
                        .value("2021-12-02T09:00:00"))
                .andExpect(jsonPath("$.items[4].lastMessage").value("Hello102"))
                .andExpect(jsonPath("$.items[4].image").value("link103"))

                .andExpect(jsonPath("$.items[5].id").value(101))
                .andExpect(jsonPath("$.items[5].name").value("NickName102"))
                .andExpect(jsonPath("$.items[5].persistDateTimeLastMessage")
                        .value("2021-12-01T09:00:00"))
                .andExpect(jsonPath("$.items[5].lastMessage").value("Hello101"))
                .andExpect(jsonPath("$.items[5].image").value("link102"));

    }
}
