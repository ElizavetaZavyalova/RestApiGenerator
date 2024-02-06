package org.example;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.annotations.RestApiGenerator;


@Slf4j
@RestApiGenerator(jsonPath = "resources/api.json")//не видит если не full path
public class Main {
    public static void main(String[] args) {
           log.info("hello-world");
    }
}