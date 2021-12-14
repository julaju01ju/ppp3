package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.javamentor.qa.platform.webapp.configs.JmApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "test/resources/application.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/UserControllerTest/user_entity.yml", "dataset/UserControllerTest/answer.yml",
            "dataset/UserControllerTest/question.yml", "dataset/UserControllerTest/reputation.yml," +
            "dataset/UserControllerTest/role.yml"})
    public void getAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/allUsers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].about").value("About"))
                .andExpect(jsonPath("$[0].city").value("City"))
                .andExpect(jsonPath("$[0].email").value("email@gmail.com"))
                .andExpect(jsonPath("$[0].fullName").value("Ivan"))
                .andExpect(jsonPath("$[0].nickname").value("NickName"))
                .andExpect(jsonPath("$[0].password").value("password"));
    }
}
