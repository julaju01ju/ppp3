package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class TestAnswerResourceController
        extends AbstractControllerTest {

    @Autowired
    private EntityManager entityManager;


    @Test
    @DataSet(value = {
            "dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml"
    },  disableConstraints = true, cleanBefore = true)
    public void getAllAnswerDtosByQustionId() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                get("/api/user/question/102/answer")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].userId").value(101))
                .andExpect(jsonPath("$[0].userReputation").value(102))
                .andExpect(jsonPath("$[0].questionId").value(102))
                .andExpect(jsonPath("$[0].body").value("Some Body"))
                .andExpect(jsonPath("$[0].persistDate").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$[0].isHelpful").value("true"))
                .andExpect(jsonPath("$[0].isDeleted").value("false"))
                .andExpect(jsonPath("$[0].dateAccept").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$[0].image").value("image"))
                .andExpect(jsonPath("$[0].nickName").value("USR"));
    }

    //ошибка в контроллере
    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml",
    }, disableConstraints = true, cleanBefore = true)
    public void shouldGetNullAnswerByQuestionId() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        get("/api/user/question/105/answer")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(jsonPath("$.id").doesNotExist());
    }


    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void deleteAnswer_OK() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        delete("/api/user/question/102/answer/102").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void deleteAnswer_t() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        delete("/api/user/question/102/answer/t").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    //ошибка в контроллере (сделать ответ 404)
    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml",
    },
            disableConstraints = true, cleanBefore = true)
    public void deleteGetAnswerByQuestionIdNotFound() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        get("/api/user/question/102/answer").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        delete("/api/user/question/102/answer/102").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        delete("/api/user/question/102/answer/103").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        delete("/api/user/question/102/answer/104").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/user/question/102/answer").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/answervote.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void deleteAnswer_1000() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(
                        delete("/api/user/question/1/answer/1000").header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/votes_on_answers.yml",
            "dataset/AnswerResourceController/questions.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void postUpVoteAnswerStatusOk() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru","USER");
        mockMvc.perform(
                        post("/api/user/question/102/answer/104/upVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("1"))
                .andExpect(status().isOk());
        Assertions.assertNotNull(entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.answer.id =:answerId AND va.user.id =: userId", VoteAnswer.class)
                .setParameter("answerId", 104L)
                .setParameter("userId", 101L));
    }

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/votes_on_answers.yml",
            "dataset/AnswerResourceController/questions.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void postDownVoteAnswerStatusOk() throws Exception {

        String USER_TOKEN = super.getToken("user@mail.ru","USER");

        mockMvc.perform(
                        post("/api/user/question/102/answer/102/downVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("1"))
                .andExpect(status().isOk());
        Assertions.assertNotNull(entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.answer.id =:answerId AND va.user.id =: userId", VoteAnswer.class)
                .setParameter("answerId", 102L)
                .setParameter("userId", 101L));
    }

    @Test@DataSet(value = {
            "dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/votes_on_answers.yml"
    }, disableConstraints = true, cleanBefore = true )
    public void checkReVoteTheAnswer() throws Exception{

        String USER_TOKEN = super.getToken("user@mail.ru","USER");

        mockMvc.perform(
                        post("/api/user/question/102/answer/103/upVote")
                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
