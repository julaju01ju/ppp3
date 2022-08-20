package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestCommentResourceController extends AbstractControllerTest{

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/comment.yml",
            "dataset/QuestionResourceController/comment_question.yml",
            "dataset/QuestionResourceController/answerVote.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getQuestionByIdAndCheckSortCommentDESC() throws Exception {



        String USER_TOKEN = super.getToken("SomeEmail@mail.mail", "someHardPassword");

        mockMvc.perform(
                        get("/api/user/question/101")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))

                .andExpect(jsonPath("$.listCommentDto[0].id").value(105))
                .andExpect(jsonPath("$.listCommentDto[0].comment").value("Some text of comment 105"))
                .andExpect(jsonPath("$.listCommentDto[0].userId").value(103))
                .andExpect(jsonPath("$.listCommentDto[1].id").value(104))
                .andExpect(jsonPath("$.listCommentDto[1].comment").value("Some text of comment 104"))
                .andExpect(jsonPath("$.listCommentDto[1].userId").value(102))
                .andExpect(jsonPath("$.listCommentDto[1].fullName").value("Constantin"))
                .andExpect(jsonPath("$.listCommentDto[2].id").value(103))
                .andExpect(jsonPath("$.listCommentDto[2].comment").value("Some text of comment 103"))
                .andExpect(jsonPath("$.listCommentDto[2].userId").value(103))
                .andExpect(jsonPath("$.listCommentDto[3].id").value(102))
                .andExpect(jsonPath("$.listCommentDto[3].comment").value("Some text of comment 102"))
                .andExpect(jsonPath("$.listCommentDto[3].userId").value(102))
                .andExpect(jsonPath("$.isUserAnswerVote").value(true));
        mockMvc.perform(
                        get("/api/user/question/102")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(jsonPath("$.isUserAnswerVote").value(false));
    }

    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/addCommentByQuestionId/users.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/role.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/questions.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/reputations.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addCommentByQuestionId() throws Exception {
        String USER_TOKEN1 = super.getToken("privet@mail.ru", "USER");

        mockMvc.perform(post("/api/user/question/100/comment")
                        .content("В любой не понятной ситуации пей чай =)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, USER_TOKEN1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value("В любой не понятной ситуации пей чай =)"))
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.fullName").value("USER"))
                .andExpect(jsonPath("$.reputation").value(5));

        String USER_TOKEN2 = super.getToken("user1@mail.ru", "USER");

        mockMvc.perform(post("/api/user/question/100/comment")
                        .content("У лукоморья дуб зеленый, златая цепь на дубе том.")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, USER_TOKEN2))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.comment").value("У лукоморья дуб зеленый, златая цепь на дубе том."))
                .andExpect(jsonPath("$.userId").value(101))
                .andExpect(jsonPath("$.fullName").value("USER1"))
                .andExpect(jsonPath("$.reputation").value(10));
    }

    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/addCommentByQuestionId/users.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/role.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/questions.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/reputations.yml",
    }, cleanBefore = true)
    public void addCommentByQuestionIdNotFound() throws Exception {
        String USER_TOKEN = super.getToken("privet@mail.ru", "USER");

        mockMvc.perform(post("/api/user/question/103/comment")
                        .content("В любой не понятной ситуации пей чай =)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/addCommentByQuestionId/users.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/role.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/questions.yml",
            "dataset/QuestionResourceController/addCommentByQuestionId/reputations.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void addCommentEmptyByQuestionId() throws Exception {
        String USER_TOKEN = super.getToken("privet@mail.ru", "USER");


        mockMvc.perform(post("/api/user/question/102/comment")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
