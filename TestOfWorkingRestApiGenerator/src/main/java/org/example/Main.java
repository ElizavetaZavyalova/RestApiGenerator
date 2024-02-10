package org.example;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.annotations.RestApiGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@Slf4j
@RestApiGenerator(jsonPath = "resources/api.json")//не видит если не full path
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}