package com.javamentor.qa.platform;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;

import com.github.database.rider.core.util.EntityManagerProvider;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.entity.user.User;
import org.junit.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@DBUnit(cacheConnection = false, leakHunter = true)
@DBRider
//@ExtendWith(DBUnitExtension.class)
//@RunWith(JUnitPlatform.class)
//@DataSet(cleanBefore = true)
public class TestTemplate {

    // tag::rules[]

//    @BeforeClass
//    public static void initDB(){
//        //trigger db creation
//        EntityManagerProvider.instance("KATA");
//    }

    @Rule
    public EntityManagerProvider emProvider = EntityManagerProvider.instance("KATA"); //<1>

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection()); //<2>
    // end::rules[]
//
//    @Test
//    @DataSet(value ="answer.yml", strategy = SeedStrategy.UPDATE,
//            disableConstraints = true,cleanAfter = true,transactional = true)
//    public void shouldLoadDataSetConfigFromAnnotation(){
//
//    }

    @Test
    @DataSet(value ="user.yaml", strategy = SeedStrategy.UPDATE,
            disableConstraints = true, cleanAfter = true,
            useSequenceFiltering = true, tableOrdering = {"ROLE"},
            transactional = true)
     public void shouldSeedDataSetDisablingContraints() {
        User user = (User) EntityManagerProvider.em().createQuery("select u from User u where u.id = 1").getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }
//
//    @Test
//    @DataSet(value = "user.yaml", cleanBefore = true)
//    public void shouldSeedDatabase() {
//        List<User> users = EntityManagerProvider.em().createQuery("select u from User u ").getResultList();
//        assertThat(users).
//                isNotNull().
//                isNotEmpty().
//                hasSize(2);
//    }

//        @Test
//        @DataSet(value = "datasets/yml/users.yml", executeStatementsBefore = "SET DATABASE REFERENTIAL INTEGRITY FALSE;", executeStatementsAfter = "SET DATABASE REFERENTIAL INTEGRITY TRUE;")
//        public void shouldSeedDataSetDisablingContraintsViaStatement() {
//            User user = (User) EntityManagerProvider.em().createQuery("select u from User u join fetch u.tweets join fetch u.followers join fetch u.tweets join fetch u.followers where u.id = 1").getSingleResult();
//            assertThat(user).isNotNull();
//            assertThat(user.getId()).isEqualTo(1);
//            assertThat(user.getTweets()).hasSize(1);
//        }


//        @Test
//        @DataSet(value = "datasets/yml/users.yml",
//                useSequenceFiltering = false,
//                tableOrdering = {"USER","TWEET","FOLLOWER"},
//                executeStatementsBefore = {"DELETE FROM FOLLOWER","DELETE FROM TWEET","DELETE FROM USER"}//needed because other tests created user dataset
//        )
//        public void shouldSeedDataSetUsingTableCreationOrder() {
//            List<User> users =  EntityManagerProvider.em().createQuery("select u from User u left join fetch u.tweets left join fetch u.followers").getResultList();
//            assertThat(users).hasSize(2);
//        }


    // tag::seedDatabase[]
//        @Test
//        @DataSet(value = "datasets/yml/users.yml", useSequenceFiltering = true)
//        public void shouldSeedUserDataSet() {
//            User user = (User) EntityManagerProvider.em().
//                    createQuery("select u from User u join fetch u.tweets join fetch u.followers where u.id = 1").getSingleResult();
//            assertThat(user).isNotNull();
//            assertThat(user.getId()).isEqualTo(1);
//            assertThat(user.getTweets()).isNotNull().hasSize(1);
//            Tweet tweet = user.getTweets().get(0);
//            assertThat(tweet).isNotNull();
//            Calendar date = tweet.getDate();
//            Calendar now = Calendar.getInstance();
//            assertThat(date.get(Calendar.DAY_OF_MONTH)).
//                    isEqualTo(now.get(Calendar.DAY_OF_MONTH));
//        }
    // end::seedDatabase[]

//        @Test
//        @DataSet(value = "datasets/yml/users.yml", strategy = SeedStrategy.TRUNCATE_INSERT, useSequenceFiltering = true)
//        public void shouldSeedUserDataSetUsingTruncateInsert(){
//            List<User> users  = EntityManagerProvider.em("rules-it").createQuery("select u from User u", User.class).getResultList();
//            assertThat(users).isNotNull();
//            assertThat(users.size()).isEqualTo(2);
//            assertThat(users.get(0).getId()).isEqualTo(1);
//            assertThat(users.get(0).get()).isEqualTo("@realpestano");
//            assertThat(users.get(1).getId()).isEqualTo(2);
//            assertThat(users.get(1).getName()).isEqualTo("@dbunit");
//        }

//
//    @AfterClass//optional
//    public static void close() throws SQLException {
//        DataSetExecutorImpl.getExecutorById(DataSetExecutorImpl.DEFAULT_EXECUTOR_ID).getRiderDataSource().getDBUnitConnection().getConnection().close();
//    }

}

