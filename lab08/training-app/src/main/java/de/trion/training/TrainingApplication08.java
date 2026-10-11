package de.trion.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableJpaAuditing
@EnableWebSecurity(debug = true)
@SpringBootApplication
public class TrainingApplication08
{

   public static void main(String[] args) {
      SpringApplication.run(TrainingApplication08.class, args);
   }

   @Bean
   RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
      return restTemplateBuilder
            .connectTimeout(Duration.ofMillis(1_500))
            .readTimeout(Duration.ofSeconds(2))
            .build();
   }
}
