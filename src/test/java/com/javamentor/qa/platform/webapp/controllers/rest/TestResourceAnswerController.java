package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestResourceAnswerController extends AbstractConrollersTests{

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/roles.yml",
            "dataset/AnswerResourceController/answervote.yml"
    })
    public void getAllAnswerDtosByQustionId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/102/answer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].userId").value(102))
                .andExpect(jsonPath("$[0].userReputation").value(102))
                .andExpect(jsonPath("$[0].questionId").value(102))
                .andExpect(jsonPath("$[0].body").value("Some Body"))
                .andExpect(jsonPath("$[0].persistDate").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$[0].isHelpful").value("true"))
                .andExpect(jsonPath("$[0].dateAccept").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$[0].countValuable").value(1))
                .andExpect(jsonPath("$[0].image").value("link"))
                .andExpect(jsonPath("$[0].nickName").value("NickName"));
    }

    @Test
    @DataSet(value = {"dataset/AnswerResourceController/users.yml",
            "dataset/AnswerResourceController/answers.yml",
            "dataset/AnswerResourceController/questions.yml",
            "dataset/AnswerResourceController/reputations.yml",
            "dataset/AnswerResourceController/roles.yml",
            "dataset/AnswerResourceController/answervote.yml"
    })
    void shouldNotGetAnswerByQuestionId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/105/answer"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}
