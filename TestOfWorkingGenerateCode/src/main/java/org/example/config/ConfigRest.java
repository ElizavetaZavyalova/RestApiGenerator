package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigRest {
  @Bean
  public OpenAPI usersMicroserviceOpenAPI(@Value("${restApi.openApi.title:}") String title,
      @Value("${restApi.openApi.description:}") String description,
      @Value("${restApi.openApi.version:}") String version) {
    return new OpenAPI().info(
            new Info().title(title).description(description).version(version)
    );
  }
}
