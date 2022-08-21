package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.CreateGroupChatDto;
import com.javamentor.qa.platform.models.dto.CreateSingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

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

            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/chat/101/single/message?page=1&items=4&sortAscendingFlag=false")
                            .header(AUTHORIZATION, USER_TOKEN))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items[0].persistDateTime").value("2022-06-05T03:00:00"))
                    .andExpect(jsonPath("$.items[1].persistDateTime").value("2022-06-04T03:00:00"))
                    .andExpect(jsonPath("$.items[2].persistDateTime").value("2022-06-03T03:00:00"))
                    .andExpect(jsonPath("$.items[3].persistDateTime").value("2022-06-02T03:00:00"));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/chat/101/single/message?page=1&items=4&sortAscendingFlag=true")
                            .header(AUTHORIZATION, USER_TOKEN))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items[3].persistDateTime").value("2022-06-04T03:00:00"))
                    .andExpect(jsonPath("$.items[2].persistDateTime").value("2022-06-03T03:00:00"))
                    .andExpect(jsonPath("$.items[1].persistDateTime").value("2022-06-02T03:00:00"))
                    .andExpect(jsonPath("$.items[0].persistDateTime").value("2022-06-01T03:00:00"));

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
                .andExpect(jsonPath("$.page.items[2].persistDateTime").value("2022-06-14T03:00:00"));
        ;

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

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createGroupChatDtoWithoutUserIds() throws Exception {

        CreateGroupChatDto createGroupChatDto = new CreateGroupChatDto();
        createGroupChatDto.setChatName("Test");

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                post("/api/user/chat/group")
                        .header(AUTHORIZATION, USER_TOKEN)
                        .content(new ObjectMapper().writeValueAsString(createGroupChatDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createGroupChatDtoWithEmptyUserIds() throws Exception {

        CreateGroupChatDto createGroupChatDto = new CreateGroupChatDto();
        createGroupChatDto.setChatName("Test");
        createGroupChatDto.setUserIds(new ArrayList<Long>());

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/group")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createGroupChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createGroupChatDtoWithoutChatName() throws Exception {

        CreateGroupChatDto createGroupChatDto = new CreateGroupChatDto();
        List<Long> userIds = new ArrayList<>();
        userIds.add(101L);
        userIds.add(102L);
        userIds.add(103L);
        createGroupChatDto.setUserIds(userIds);

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/group")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createGroupChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Assertions.assertNotNull(entityManager.createQuery("from GroupChat", GroupChat.class));
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createGroupChatDtoWithUserIdsAndChatName() throws Exception {

        CreateGroupChatDto createGroupChatDto = new CreateGroupChatDto();
        List<Long> userIds = new ArrayList<>();
        userIds.add(101L);
        userIds.add(102L);
        userIds.add(103L);
        createGroupChatDto.setUserIds(userIds);
        createGroupChatDto.setChatName("Test");

        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/group")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createGroupChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        Assertions.assertNotNull(entityManager.createQuery("from GroupChat", GroupChat.class));
    }


    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/role.yml",
            "dataset/ChatResourceController/users.yml",
            "dataset/ChatResourceController/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createSingleChatDtoWithWrongUserId() throws Exception {

        CreateSingleChatDto createSingleChatDto = new CreateSingleChatDto();
        createSingleChatDto.setUserRecipientId(1001L);

        String USER_TOKEN = super.getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/single")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createSingleChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/role.yml",
            "dataset/ChatResourceController/users.yml",
            "dataset/ChatResourceController/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createSingleChatDtoWithEmptyMessage() throws Exception {

        CreateSingleChatDto createSingleChatDto = new CreateSingleChatDto();
        createSingleChatDto.setMessage(" ");

        String USER_TOKEN = super.getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/single")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createSingleChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }



    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/role.yml",
            "dataset/ChatResourceController/users.yml",
            "dataset/ChatResourceController/messages.yml",
            "dataset/ChatResourceController/singleChats.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void createSingleChatDtoWithUserIdAndMessages() throws Exception {

        CreateSingleChatDto createSingleChatDto = new CreateSingleChatDto();
        createSingleChatDto.setUserRecipientId(101L);
        createSingleChatDto.setMessage("Hello101");

        String USER_TOKEN = super.getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        post("/api/user/chat/single")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(createSingleChatDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        Assertions.assertNotNull(entityManager.createQuery("from SingleChat", SingleChat.class));
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addUserToGroupChatUserDoesNotExist() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");
        mockMvc.perform(
                post("/api/user/chat/group/101/join?userId=110")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addUserToGroupChatGroupChatDoesNotExist() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");
        mockMvc.perform(
                        post("/api/user/chat/group/110/join?userId=101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addUserToGroupChatUserExistsAndGroupChatExists() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");
        mockMvc.perform(
                        post("/api/user/chat/group/101/join?userId=101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Assertions.assertEquals(BigInteger.valueOf(101), entityManager.createNativeQuery("select user_id from groupchat_has_users where chat_id = 101").getSingleResult());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/GroupChat/role.yml",
            "dataset/ChatResourceController/GroupChat/users.yml",
            "dataset/ChatResourceController/GroupChat/chat.yml",
            "dataset/ChatResourceController/GroupChat/groupChat.yml",
            "dataset/ChatResourceController/GroupChat/messages.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addUserToGroupChatUserAlreadyAddedToGroupChat() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "USER");
        mockMvc.perform(
                        post("/api/user/chat/group/101/join?userId=101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/api/user/chat/group/101/join?userId=101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/chat.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/singleChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/messages.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/users.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/role.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true )
    public void getFindChatWithValidParametersInSingleAndGroupChatByTitle() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                        get("/api/user/chat/?findMessage=chat")
                                .header(AUTHORIZATION, USER_TOKEN))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Some single chat 1"))
                .andExpect(jsonPath("$[0].image").value("link102"))
                .andExpect(jsonPath("$[0].lastMessage").value("Hello user 1 in single chat number 3"))
                .andExpect(jsonPath("$[0].persistDateTimeLastMessage").value("2022-07-04T20:40:30"))

                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Some group chat 1"))
                .andExpect(jsonPath("$[1].image").value("Some image in Group chat 1"))
                .andExpect(jsonPath("$[1].lastMessage").value("Hello users in group chat 1, from user number 3"))
                .andExpect(jsonPath("$[1].persistDateTimeLastMessage").value("2022-07-01T07:55:21"));
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/chat.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/singleChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/messages.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/users.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/role.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void getFindChatWithValidParametersUserHasOnlySingleChat() throws Exception {
        String USER_TOKEN = super.getToken("user4@mail.ru", "pass4");

        mockMvc.perform(
                get("/api/user/chat/?findMessage=chat")
                        .header(AUTHORIZATION, USER_TOKEN))
        .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].name").value("Some single chat 2"))
                .andExpect(jsonPath("$[0].image").value("link103"))
                .andExpect(jsonPath("$[0].lastMessage").value("Hello user 2 in single chat number 4"))
                .andExpect(jsonPath("$[0].persistDateTimeLastMessage").value("2021-12-05T05:00:00"));
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/chat.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/singleChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/messages.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/users.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/role.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true)

    public void getFindChatWithValidParametersUserHasOnlyGroupChat() throws Exception{
        String USER_TOKEN = super.getToken("user5@mail.ru", "pass5");

        mockMvc.perform(
                        get("/api/user/chat/?findMessage=chat")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].name").value("Some group chat 5"))
                .andExpect(jsonPath("$[0].image").value("Some image in group Chat 5"))
                .andExpect(jsonPath("$[0].lastMessage").value("Hello users in group chat 5, from user number 5"))
                .andExpect(jsonPath("$[0].persistDateTimeLastMessage").value("2022-07-05T20:00:00"));
    }


    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/chat.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/singleChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/messages.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/users.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/role.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true)

    public void getFindStringWithoutParametersInSingleAndGroupChat() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                        get("/api/user/chat")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/chat.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/singleChats.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/messages.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/users.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/role.yml",
            "dataset/ChatResourceController/getStringInGroupAndSingleChats/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true)

    public void getFindStringWithIncorrectParameters() throws Exception {
        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        mockMvc.perform(
                        get("/api/user/chat/?findMessage=153")
                                .header(AUTHORIZATION, USER_TOKEN)).andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DataSet(value = {
            "dataset/ChatResourceController/deleteChatById/chat.yml",
            "dataset/ChatResourceController/deleteChatById/groupChats.yml",
            "dataset/ChatResourceController/deleteChatById/singleChats.yml",
            "dataset/ChatResourceController/deleteChatById/messages.yml",
            "dataset/ChatResourceController/deleteChatById/users.yml",
            "dataset/ChatResourceController/deleteChatById/role.yml",
            "dataset/ChatResourceController/deleteChatById/groupChatHasUsers.yml"
    }, disableConstraints = true, cleanBefore = true)
    @Transactional
    public void deleteChatById() throws Exception {

        String USER_TOKEN = super.getToken("user1@mail.ru", "pass0");

        //SingleChat
        mockMvc.perform(
                        delete("/api/user/chat/3")
                                .header(AUTHORIZATION, USER_TOKEN)).andDo(print())
                .andExpect(status().isOk());

        //GlobalChat
        mockMvc.perform(
                        delete("/api/user/chat/6")
                                .header(AUTHORIZATION, USER_TOKEN)).andDo(print())
                .andExpect(status().isOk());

        //nonExistentChat
        mockMvc.perform(
                        delete("/api/user/chat/99")
                                .header(AUTHORIZATION, USER_TOKEN)).andDo(print())
                .andExpect(status().isNotFound());

    }

}
