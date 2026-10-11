package de.trion.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableJpaAuditing
@SpringBootApplication
public class TrainingApplication14
{

   public static void main(String[] args) {
      SpringApplication.run(TrainingApplication14.class, args);
   }

   @Bean
   RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
      return restTemplateBuilder
            .connectTimeout(Duration.ofMillis(1_500))
            .readTimeout(Duration.ofSeconds(2))
            .build();
   }
}
