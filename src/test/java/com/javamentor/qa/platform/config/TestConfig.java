package com.javamentor.qa.platform.config;

import javax.sql.DataSource;

import com.javamentor.qa.platform.dao.impl.model.UserRepository;
//import com.javamentor.qa.platform.dbutil.EntityUtils;
import com.javamentor.qa.platform.dbutil.EntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@Import(EntityRepository.class)
public class TestConfig {

    @Bean(name = "data-source-1")
    @Primary
    public DataSource dataSourcePrimary() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .setScriptEncoding("UTF-8")
                .addScript("schema.sql")
                .build();
    }

//    @Bean(name = "data-source-2")
//    public DataSource dataSourceSecondary() {
//        return new EmbeddedDatabaseBuilder()
//                .generateUniqueName(true)
//                .setType(EmbeddedDatabaseType.HSQL)
//                .setScriptEncoding("UTF-8")
//                .addScript("schema2.sql")
//                .build();
//    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
