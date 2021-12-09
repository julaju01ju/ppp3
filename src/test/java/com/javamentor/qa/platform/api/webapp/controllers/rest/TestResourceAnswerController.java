package com.javamentor.qa.platform.api.webapp.controllers.rest;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.api.webapp.configs.JmApplication;
import com.javamentor.qa.platform.api.webapp.controllers.rest.ResourceAnswerController;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@ContextConfiguration
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestResourceAnswerController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceAnswerController resourceAnswerController;

    @Test
    public void test(){
        assertThat(resourceAnswerController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }


//    @Test
//    @DataSet(value = {"dataset/resource/user.yml",
//            "dataset/resourceAnswerController/answers.yml",
//            "dataset/resourceAnswerController/questions.yml",
//            "dataset/resourceAnswerController/reputations.yml",
//            "dataset/resourceAnswerController/roles.yml"})
//    public void getAnswerByQuestionId() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/100/answer"))
//                .andDo(print())
//                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(101))
//                .andExpect(jsonPath("$.email").value("SomeEmail@mail.mail"))
//                .andExpect(jsonPath("$.fullName").value("Constantin"))
//                .andExpect(jsonPath("$.linkImage").value("link"))
//                .andExpect(jsonPath("$.city").value("Moscow"))
//                .andExpect(jsonPath("$.reputation").value(101));
//    }

}

