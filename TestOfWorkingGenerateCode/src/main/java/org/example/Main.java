package org.example;



import org.example.processors.annotations.RestApiGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@RestApiGenerator(jsonPath = "P:\\Projects\\IntellijIDEA\\vkr\\RestApiGenerator\\TestOfWorkingGenerateCode\\src\\main\\resources\\books\\main.json")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

//http://localhost:8080/swagger-ui/index.html