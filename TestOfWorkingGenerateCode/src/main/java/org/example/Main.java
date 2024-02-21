package org.example;

import org.example.processors.annotations.RestApiGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestApiGenerator(jsonPath = "P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\TestOfWorkingGenerateCode\\src\\main\\resources\\generate.json")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}