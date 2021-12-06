package com.javamentor.qa.platform;

import com.javamentor.qa.platform.api.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.api.dao.impl.dto.AnswerDtoDaoImpl;
import com.javamentor.qa.platform.api.webapp.configs.JmApplication;
import com.javamentor.qa.platform.api.webapp.controllers.MainController;
import com.javamentor.qa.platform.api.webapp.controllers.rest.ResourceAnswerConetoller;
import org.jboss.jandex.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmApplication.class)
public class TestResourceAnswerController {

    @Autowired
    private MainController mainController;

    @Test
    public void test() throws Exception {
        assertThat(mainController).isNotNull();
    }
}
