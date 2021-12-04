package com.javamentor.qa.platform.dbutil;

import com.javamentor.qa.platform.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class EntityUtils {

    private static final String INSERT_QUERY = "INSERT INTO Entity (value) VALUES (?)";
    private static final String SELECT_QUERY = "SELECT user FROM Entity";

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public EntityUtils(DataSource dataSource, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(transactionManager);

    }
//
    public void addValues(String... values) {
        for (String val : values) {
            jdbcTemplate.update(INSERT_QUERY, val);
        }
    }

//    public void executeInTransaction(TransactionCallback<?> callback) {
//        transactionTemplate.execute(callback);
//    }

    public void assertValues(String... values) {
        Set<String> expected = new HashSet<>(Arrays.asList(values));
        Set<String> actual = new HashSet<>(jdbcTemplate.queryForList(SELECT_QUERY, String.class));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

//    public void showAll() {
////        Entity entity =  jdbcTemplate.query(1);
//        List<String> data=jdbcTemplate.queryForList(SELECT_QUERY,String.class);
//        data.forEach(System.out::println);
//        System.out.println("===================");
//        System.out.println(data);
//        System.out.println("====================");
//    }
}
