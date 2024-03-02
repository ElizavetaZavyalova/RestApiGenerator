package org.example.controller;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;


import javax.sql.DataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Testcontainers
@ContextConfiguration(initializers = {BaseTest.Initializer.class})
class BaseTest {
    static int containerPort = 5432;
    static int localPort = 5432;
    @Container
    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>(DockerImageName.parse("postgres"))
            .withDatabaseName("test_db")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("DB.sql")
            .withReuse(true)
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQL.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQL.getUsername(),
                    "spring.datasource.password=" + postgreSQL.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
    protected DataSource dataSource;

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();


    void init() {
        dataSource = Optional.ofNullable(dataSource).orElse(createConnectionPool());
        try {
            dsl= Optional.ofNullable(dsl).orElse(createContext());
            executeScript("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\TestOfWorkingGenerateCode\\src\\test\\resources\\init.sql");
        } catch (SQLException e) {
            log.info(e.getMessage());
            assert false;
        }
    }

    DSLContext dsl=null;

    protected DSLContext createContext() throws SQLException {
      return DSL.using(dataSource.getConnection(), SQLDialect.POSTGRES);
    }

   protected void executeScript(String file){
       try {
           String sql = new String(Files.readAllBytes(Paths.get(file)));
           dsl.execute(sql);
       } catch (IOException e) {
           log.info(e.getMessage());
           assert false;
       }
    }
    void clear(){
        executeScript("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\TestOfWorkingGenerateCode\\src\\test\\resources\\clear.sql");
    }

    protected HikariDataSource createConnectionPool() {
        log.info("Create connection pool");
        var config = new HikariConfig();
        config.setJdbcUrl(postgreSQL.getJdbcUrl());
        config.setUsername(postgreSQL.getUsername());
        config.setPassword(postgreSQL.getPassword());
        config.setDriverClassName(postgreSQL.getDriverClassName());
        return new HikariDataSource(config);
    }
    protected String toJson(Object object) {
        String json = "{}";
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error convert object to json", e);
        }
        return json;
    }

}