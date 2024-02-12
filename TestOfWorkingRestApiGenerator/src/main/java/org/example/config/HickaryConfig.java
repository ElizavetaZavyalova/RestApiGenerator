package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
public class HickaryConfig {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSqlException(SQLException e) {
        // Логика обработки исключения
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка SQL: " + e.getMessage());
    }


    DSLContext makeHikariConfig(String url,  String pasword,String user,String driver,String dialect) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pasword);
        hikariConfig.setDriverClassName(driver);
        DSLContext context= DSL.using(new HikariDataSource(hikariConfig), SQLDialect.valueOf(dialect));



    }

}
