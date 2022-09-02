package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 02.12.2021
 */

public class TestUserResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserById() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.email").value("Constantin@mail.mail"))
                .andExpect(jsonPath("$.fullName").value("Constantin"))
                .andExpect(jsonPath("$.linkImage").value("linkTest1"))
                .andExpect(jsonPath("$.city").value("TestCity1"))
                .andExpect(jsonPath("$.reputation").value(10));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/users.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/answers.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/questions.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/reputations.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/question_has_tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/roles.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/votes_on_answers.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserByIdWithTop3Tags() throws Exception {

        String USER_TOKEN = getToken("user_01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user_01@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("USER"))
                .andExpect(jsonPath("$.linkImage").value("image"))
                .andExpect(jsonPath("$.city").value("city"))
                .andExpect(jsonPath("$.reputation").value(42))
                .andExpect(jsonPath("$.topTags.length()").value(3))
                .andExpect(jsonPath("$.topTags[0].name").value("spring"))
                .andExpect(jsonPath("$.topTags[1].name").value("hibernate"))
                .andExpect(jsonPath("$.topTags[2].name").value("flyway"));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/users.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/answers.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/questions.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/reputations.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/question_has_tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/roles.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/votes_on_answers.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserByIdWithTop3TagsUserWithoutReputations() throws Exception {

        String USER_TOKEN = getToken("user_01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/2")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("SomeEmail@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Constantin"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(0))
                .andExpect(jsonPath("$.topTags.length()").value(0));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/users.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/answers.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/questions.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/reputations.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/question_has_tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/roles.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/votes_on_answers.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserByIdWithTop3TagsUserWithDownVotesMustNotBeReputation() throws Exception {

        String USER_TOKEN = getToken("user_01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("toxic@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Oleg"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(0))
                .andExpect(jsonPath("$.topTags.length()").value(0));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/users.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/answers.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/questions.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/reputations.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/question_has_tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/roles.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/votes_on_answers.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserByIdWithTop3TagsNotExistsId() throws Exception {

        String USER_TOKEN = getToken("user_01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/999")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"}, disableConstraints = true, cleanBefore = true)
    public void shouldNotGetUserById() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/120")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithOutPageParam() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithOutItemsParam() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }


    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage2Items1() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=2&items=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(20))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(1))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 1);
        Assertions.assertTrue((int) list.get(0).get("id") == 118);
    }


    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items3AndFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=3&filter=@mail")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(7))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].id").value(119))
                .andExpect(jsonPath("$.items[0].email").value("Farhad@mail.mail"))
                .andExpect(jsonPath("$.items[0].fullName").value("Farhad"))
                .andExpect(jsonPath("$.items[1].id").value(118))
                .andExpect(jsonPath("$.items[1].email").value("Leonid@mail.mail"))
                .andExpect(jsonPath("$.items[1].fullName").value("Leonid"))
                .andExpect(jsonPath("$.items[2].id").value(117))
                .andExpect(jsonPath("$.items[2].email").value("Denis@mail.mail"))
                .andExpect(jsonPath("$.items[2].fullName").value("Denis"));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items3AndEmptyFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=3&filter=")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(7))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].id").value(119))
                .andExpect(jsonPath("$.items[0].email").value("Farhad@mail.mail"))
                .andExpect(jsonPath("$.items[0].fullName").value("Farhad"));
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items50() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=50")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(50))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 20);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items5CheckSorting() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue((int) list.get(0).get("id") == 119);
        Assertions.assertTrue((int) list.get(1).get("id") == 118);
        Assertions.assertTrue((int) list.get(2).get("id") == 117);
        Assertions.assertTrue((int) list.get(3).get("id") == 116);
        Assertions.assertTrue((int) list.get(4).get("id") == 115);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputation() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("USER"))
                .andExpect(jsonPath("$.items[0].linkImage").value("image"))
                .andExpect(jsonPath("$.items[0].city").value("city"))
                .andExpect(jsonPath("$.items[0].reputation").value(0))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputationWithFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5&filter=mail")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("USER"))
                .andExpect(jsonPath("$.items[0].linkImage").value("image"))
                .andExpect(jsonPath("$.items[0].city").value("city"))
                .andExpect(jsonPath("$.items[0].reputation").value(0))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputationWithEmptyFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5&filter=")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("USER"))
                .andExpect(jsonPath("$.items[0].linkImage").value("image"))
                .andExpect(jsonPath("$.items[0].city").value("city"))
                .andExpect(jsonPath("$.items[0].reputation").value(0))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersSortedByVote/roles.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/users.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/reputations.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/answers.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_answers.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void getPageAllUsersSortedByVoteCheckSortingDESCWithPage1Items5() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/vote?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        System.out.println(list.size());
        Assertions.assertTrue((int) list.get(0).get("id") == 105);
        Assertions.assertTrue((int) list.get(1).get("id") == 104);
        Assertions.assertTrue((int) list.get(2).get("id") == 103);
        Assertions.assertTrue((int) list.get(3).get("id") == 102);
        Assertions.assertTrue((int) list.get(4).get("id") == 101);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersSortedByVote/roles.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/users.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/reputations.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/answers.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_answers.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void GetPageAllUsersSortedByVoteCheckSortingASCWithPage4Items5() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/vote?page=4&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(4))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        System.out.println(list.size());
        Assertions.assertTrue((int) list.get(4).get("id") == 106);
        Assertions.assertTrue((int) list.get(3).get("id") == 107);
        Assertions.assertTrue((int) list.get(2).get("id") == 108);
        Assertions.assertTrue((int) list.get(1).get("id") == 109);
        Assertions.assertTrue((int) list.get(0).get("id") == 110);
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/updatePassword/users.yml"},
            disableConstraints = true, cleanBefore = true)
    public void updatePassword() throws Exception {

        String USER_TOKEN = getToken("user130@mail.ru", "USER");

        UserDtoTest userDtoTest = new UserDtoTest();
        userDtoTest.setId(130L);
        userDtoTest.setPassword("USER");

        // the same password
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is not correct(too short)
        userDtoTest.setPassword("Ty55");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is not correct(wrong symbols)
        userDtoTest.setPassword("111111111111111111");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is correct
        userDtoTest.setPassword("TtF@R1");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/getUserProfileQuestionDto/users.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/questions.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/question_has_tag.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/tag.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/answers.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/role.yml"},
            disableConstraints = true, cleanBefore = true)
    public void getUserProfileQuestionDto() throws Exception {
        String USER_TOKEN = getToken("user_ed01@mail.ru", "USER");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/profile/questions")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value(100))
                .andExpect(jsonPath("$[0].title").value("title 100"))
                .andExpect(jsonPath("$[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$[0].listTagDto.[0].name").value("TAG100"))
                .andExpect(jsonPath("$[0].listTagDto.[0].description").value("This is tag 100"))
                .andExpect(jsonPath("$[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$[0].listTagDto.[1].name").value("TAG101"))
                .andExpect(jsonPath("$[0].listTagDto.[1].description").value("This is tag 101"))
                .andExpect(jsonPath("$[0].countAnswer").value(2))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/getUserProfileQuestionDto/users.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/questions.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/question_has_tag.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/tag.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/answers.yml",
            "dataset/UserResourceController/getUserProfileQuestionDto/role.yml"},
            disableConstraints = true, cleanBefore = true)
    public void getUserProfileQuestionDtoEmpty() throws Exception {
        String USER_TOKEN = getToken("user_null@mail.ru", "USER");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/profile/questions")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetBookMarksUsers/role.yml",
            "dataset/UserResourceController/GetBookMarksUsers/users.yml",
            "dataset/UserResourceController/GetBookMarksUsers/questions.yml",
            "dataset/UserResourceController/GetBookMarksUsers/tag.yml",
            "dataset/UserResourceController/GetBookMarksUsers/questions_has_tags.yml",
            "dataset/UserResourceController/GetBookMarksUsers/answers.yml",
            "dataset/UserResourceController/GetBookMarksUsers/views.yml",
            "dataset/UserResourceController/GetBookMarksUsers/votes_on_questions.yml",
    },
            disableConstraints = true, cleanBefore = true)

    public void getBookMarksEmpty() throws Exception {

        String USER_TOKEN = getToken("user_ed01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/profile/bookmarks")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetBookMarksUsers/role.yml",
            "dataset/UserResourceController/GetBookMarksUsers/users.yml",
            "dataset/UserResourceController/GetBookMarksUsers/questions.yml",
            "dataset/UserResourceController/GetBookMarksUsers/tag.yml",
            "dataset/UserResourceController/GetBookMarksUsers/questions_has_tags.yml",
            "dataset/UserResourceController/GetBookMarksUsers/answers.yml",
            "dataset/UserResourceController/GetBookMarksUsers/views.yml",
            "dataset/UserResourceController/GetBookMarksUsers/votes_on_questions.yml",
            "dataset/UserResourceController/GetBookMarksUsers/bookmark.yml",

    }, disableConstraints = true, cleanBefore = true)

    public void getBookMarksUsers() throws Exception {

        String USER_TOKEN = getToken("user_ed01@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/profile/bookmarks")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].questionId").value(101))
                .andExpect(jsonPath("$[0].title").value("title 101"))

                .andExpect(jsonPath("$[0].tag.[0].id").value(102))
                .andExpect(jsonPath("$[0].tag.[0].name").value("TAG102"))
                .andExpect(jsonPath("$[0].tag.[0].description").value("This is tag 102"))

                .andExpect(jsonPath("$[0].countAnswer").value(2))
                .andExpect(jsonPath("$[0].countVote").value(0))
                .andExpect(jsonPath("$[0].countView").value(1))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/users.yml",
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/questions.yml",
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/question_has_tag.yml",
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/tag.yml",
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/answers.yml",
            "dataset/UserResourceController/getUserProfileDeletedQuestionDto/role.yml"},
            disableConstraints = true, cleanBefore = true)
    public void getUserProfileDeletedQuestionDto() throws Exception {
        String USER_TOKEN = getToken("user_ed01@mail.ru", "USER");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/profile/delete/questions")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value(200))
                .andExpect(jsonPath("$[0].title").value("title 200"))
                .andExpect(jsonPath("$[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$[0].listTagDto.[0].name").value("TAG100"))
                .andExpect(jsonPath("$[0].listTagDto.[0].description").value("This is tag 100"))
                .andExpect(jsonPath("$[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$[0].listTagDto.[1].name").value("TAG101"))
                .andExpect(jsonPath("$[0].listTagDto.[1].description").value("This is tag 101"))
                .andExpect(jsonPath("$[0].countAnswer").value(1))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/TestTop10UsersAnswers/users.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/answers.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/questions.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/reputations.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getTop10UserDtoForAnswer() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getTop10UserDtoForAnswer")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$[0].fullName").value("USER"))
                .andExpect(jsonPath("$[0].linkImage").value("image"))
                .andExpect(jsonPath("$[0].city").value("city"));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/TestTop10UsersAnswers/users.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/answers.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/questions.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/reputations.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getTop10UserDtoForAnswerOnTheMonth() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getTop10UserDtoForAnswerOnTheMonth")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$[0].fullName").value("USER"))
                .andExpect(jsonPath("$[0].linkImage").value("image"))
                .andExpect(jsonPath("$[0].city").value("city"));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/TestTop10UsersAnswers/users.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/answers.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/questions.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/reputations.yml",
            "dataset/UserResourceController/TestTop10UsersAnswers/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getTop10UserDtoForAnswerOnTheYear() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getTop10UserDtoForAnswerOnTheYear")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].email").value("user@mail.ru"))
                .andExpect(jsonPath("$[0].fullName").value("USER"))
                .andExpect(jsonPath("$[0].linkImage").value("image"))
                .andExpect(jsonPath("$[0].city").value("city"));
    }


    @Test
    @DataSet(value = {
            "dataset/UserResourceController/getUserProfileReputationDto/role.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/users.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/questions.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/reputation.yml"},
            disableConstraints = true, cleanBefore = true)
    public void getUserProfileReputationDto() throws Exception {
        String USER_TOKEN = getToken("user1@mail.ru", "user");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/profile/reputation")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reputation").value(10))
                .andExpect(jsonPath("$[0].questionId").value(101))
                .andExpect(jsonPath("$[1].reputation").value(5))
                .andExpect(jsonPath("$[1].questionId").value(102))
                .andExpect(jsonPath("$[4].reputation").value(30))
                .andExpect(jsonPath("$[4].questionId").value(105))
                .andExpect(jsonPath("$.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/getUserProfileReputationDto/role.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/users.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/questions.yml",
            "dataset/UserResourceController/getUserProfileReputationDto/reputation.yml"},
            disableConstraints = true, cleanBefore = true)

    public void getUserProfileReputationDtoEmpty() throws Exception {

        String USER_TOKEN = getToken("user3@mail.ru", "user");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/profile/reputation")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}