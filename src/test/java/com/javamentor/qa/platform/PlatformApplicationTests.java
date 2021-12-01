//package com.javamentor.qa.platform;
//
//import com.github.database.rider.core.DBUnitRule;
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.github.database.rider.junit5.util.EntityManagerProvider;
//import com.javamentor.qa.platform.models.entity.user.User;
//import org.junit.Rule;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class PlatformApplicationTests {
//
//    @RunWith(JUnit4.class)
//    public class DatabaseRiderIt {
//
//        @Rule
//        public EntityManagerProvider emProvider = EntityManagerProvider.instance("rules-it");
//
//        @Rule
//        public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection());
//    }
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    @DataSet(value = "datasets/yml/users.yml", useSequenceFiltering = true)
//    public void shouldSeedUserDataSet() {
//        User user = (User) EntityManagerProvider.em().
//                createQuery("select u from User u where u.id = 1").getSingleResult();
//        assertThat(user).isNotNull();
//        assertThat(user.getId()).isEqualTo(1);
//        assertThat(user.getTweets()).isNotNull().hasSize(1);
//        Tweet tweet = user.getTweets().get(0);
//        assertThat(tweet).isNotNull();
//        Calendar date = tweet.getDate();
//        Calendar now = Calendar.getInstance();
//        assertThat(date.get(Calendar.DAY_OF_MONTH)).
//                isEqualTo(now.get(Calendar.DAY_OF_MONTH));
//
//
//    }
