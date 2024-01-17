package org.example;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.RestApiGenerator;


@Slf4j
@RestApiGenerator(jsonPath = "resources/api.json")
public class Main {
    public static void main(String[] args) {
           log.info("hello-world");
    }
}