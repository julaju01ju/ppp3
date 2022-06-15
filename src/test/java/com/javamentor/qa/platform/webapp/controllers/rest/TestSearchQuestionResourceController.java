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

public class TestSearchQuestionResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml",
            "dataset/SearchQuestionResourceController/bookmark.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionSimplyRequestSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("TAG101"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(108))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("TAG108"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(109))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("TAG109"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].title").value("title to 101"))
                .andExpect(jsonPath("$.items[0].description").value("description to 101"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Igor"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(2))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(true));

    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordUserNameSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=User:Dmitry")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(103))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[0].title").value("title to 102"))
                .andExpect(jsonPath("$.items[0].description").value("description to 102"))
                .andExpect(jsonPath("$.items[0].authorId").value(102))
                .andExpect(jsonPath("$.items[0].authorName").value("Dmitry"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(0))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordUserNameAndSimplyRequestSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=User:Dmitry 102")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(103))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[0].title").value("title to 102"))
                .andExpect(jsonPath("$.items[0].description").value("description to 102"))
                .andExpect(jsonPath("$.items[0].authorId").value(102))
                .andExpect(jsonPath("$.items[0].authorName").value("Dmitry"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(0))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordTitleSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=Title:101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("TAG101"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(108))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("TAG108"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(109))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("TAG109"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].title").value("title to 101"))
                .andExpect(jsonPath("$.items[0].description").value("description to 101"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Igor"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(2))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/bookmark.yml"
,
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordTitleAndSimplyRequestSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=Title:title 102")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(103))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[0].title").value("title to 102"))
                .andExpect(jsonPath("$.items[0].description").value("description to 102"))
                .andExpect(jsonPath("$.items[0].authorId").value(102))
                .andExpect(jsonPath("$.items[0].authorName").value("Dmitry"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(0))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordBodySortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=Body:103")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("TAG101"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("2"))
                .andExpect(jsonPath("$.items[0].id").value(103))
                .andExpect(jsonPath("$.items[0].title").value("title to 103"))
                .andExpect(jsonPath("$.items[0].description").value("description to 103"))
                .andExpect(jsonPath("$.items[0].authorId").value(103))
                .andExpect(jsonPath("$.items[0].authorName").value("Maxim"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(25))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(-1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));

    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordBodyAndSimplyRequestSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=Body:103 to 103")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("TAG101"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("2"))
                .andExpect(jsonPath("$.items[0].id").value(103))
                .andExpect(jsonPath("$.items[0].title").value("title to 103"))
                .andExpect(jsonPath("$.items[0].description").value("description to 103"))
                .andExpect(jsonPath("$.items[0].authorId").value(103))
                .andExpect(jsonPath("$.items[0].authorName").value("Maxim"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(25))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(-1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordUserNameTitleBodySortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=User:Dmitry Title:102 Body:102")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(103))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("Some name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[1].description").value("Some text here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[2].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[2].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[0].title").value("title to 102"))
                .andExpect(jsonPath("$.items[0].description").value("description to 102"))
                .andExpect(jsonPath("$.items[0].authorId").value(102))
                .andExpect(jsonPath("$.items[0].authorName").value("Dmitry"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(0))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(1))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/SearchQuestionResourceController/users.yml",
            "dataset/SearchQuestionResourceController/tag.yml",
            "dataset/SearchQuestionResourceController/votes_on_questions.yml",
            "dataset/SearchQuestionResourceController/answers.yml",
            "dataset/SearchQuestionResourceController/questions.yml",
            "dataset/SearchQuestionResourceController/question_has_tag.yml",
            "dataset/SearchQuestionResourceController/reputations.yml",
            "dataset/SearchQuestionResourceController/roles.yml",
            "dataset/SearchQuestionResourceController/comment.yml",
            "dataset/SearchQuestionResourceController/comment_question.yml",
            "dataset/SearchQuestionResourceController/bookmark.yml"


    },
            disableConstraints = true, cleanBefore = true)
    public void getSearchQuestionKeyWordUserNameTitleBodyAndSimplyRequestSortedById() throws Exception {

        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/search/?page=1&items=10&request=User:Igor Title:title Body:description 104")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description").value("Some another text here"))
                .andExpect(jsonPath("$.items[0].listTagDto.size()").value("1"))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[0].title").value("title to 104"))
                .andExpect(jsonPath("$.items[0].description").value("description to 104"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Igor"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(0))
                .andExpect(jsonPath("$.items[0].countAnswer").value(0))
                .andExpect(jsonPath("$.items[0].isUserBookMarks").value(true));

    }

}