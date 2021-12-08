package com.javamentor.qa.platform;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.api.dao.impl.dto.AnswerDtoDaoImpl;
import com.javamentor.qa.platform.api.dao.impl.model.RoleDao;
import com.javamentor.qa.platform.api.webapp.configs.JmApplication;
import com.javamentor.qa.platform.api.webapp.controllers.rest.ResourceAnswerConetoller;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DBUnit(allowEmptyFields = true, cacheConnection = false)
@DBRider
public class TestResourceAnswerController {

    @Autowired
    private ResourceAnswerConetoller resourceAnswerConetoller;

    @Autowired
    private AnswerDtoDaoImpl answerDtoDao;
    @Autowired
    private RoleDao roleDao;

    @Test
    public void test() throws Exception {
        assertThat(resourceAnswerConetoller).isNotNull();
    }

    @Test
    @DataSet(value = "/dataset/role.yaml",strategy = SeedStrategy.UPDATE,
            disableConstraints = true,cleanAfter = true)
    public void getRole(){
//        roleDao.persist();
        Optional<Role> role  = roleDao.getById(100L);
        role.ifPresent(System.out::println);

    }



}
