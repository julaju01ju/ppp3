package com.javamentor.qa.platform.datasettest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;


import com.javamentor.qa.platform.config.TestConfig;
import com.javamentor.qa.platform.dbutil.EntityRepository;
//import com.javamentor.qa.platform.dbutil.EntityUtils;
import com.javamentor.qa.platform.dbutil.EntityUtils;
import com.javamentor.qa.platform.model.Entity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;



@SpringBootTest
@RunWith(SpringRunner.class)
@DBRider
@ContextConfiguration(classes = TestConfig.class)
@DataSet(value = "test.yml")
public class DataSetTests {

    @Autowired

    private EntityUtils entityUtils;

//    @Autowired
//    private EntityRepository entityRepository;

//    @Test
//    @DataSet(cleanBefore = true)
//    public void testCleanBefore() {
//        entityUtils.assertValues();
//    }

//    @Test
//    @DataSet(value = "test.yml")
//    public void addValues(){
//        entityUtils.addValues("value1", "value2");
//    }
//
//    @Test
//    @DataSet(value = "test.yml")
//    public void testOnMethod() {
////        entityUtils.addValues("value1", "value2");
//        entityUtils.assertValues("value1", "value2");
//    }

    @Test
    @DataSet(value = "test.yml")
    public void test_show_all(){
//        entityUtils.addValues("value1", "value2");
//       Entity entity =  entityRepository.getOne(1);

        System.out.println("======================");
        entityUtils.showAll();
        System.out.println("=======================");
    }

//    @Test
//    @DataSet(value = "test.yml")
//    @Transactional
//    public void testOnMethodWithTransaction() {
//        entityUtils.assertValues("value1", "value2");
//    }
//
//    @Test
//    public void testOnClass() {
//        entityUtils.assertValues("value3", "value4");
//    }


}
