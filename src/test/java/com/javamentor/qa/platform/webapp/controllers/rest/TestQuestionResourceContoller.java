package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 15.12.2021
 */

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestQuestionResourceContoller {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
//            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/101"))
                .andExpect(status().isOk());
    }
}
