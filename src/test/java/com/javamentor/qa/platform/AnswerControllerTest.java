//package com.javamentor.qa.platform;
//
//
//import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
//import com.javamentor.qa.platform.webapp.controllers.rest.ResourceAnswerConetoller;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//@SpringBootTest
//@TestPropertySource(locations = "classpath:test.properties")
//@AutoConfigureMockMvc(secure = false)
//@Retention(RetentionPolicy.RUNTIME)
//@RunWith(SpringRunner.class)
//@WebMvcTest(ResourceAnswerConetoller.class)
//public class AnswerControllerTest {
//
//    @MockBean
//    private AnswerDtoDao answerDtoDao;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testReturn200() throws Exception {
//        mockMvc.perform(get("api/user/question/{questionId}/answer"))
//                .andExpect(status().isOk())
//                .andExpect(content().mimeType("text/html"))
//                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//        varify
//    }
//
//}
