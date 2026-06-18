package com.msa.microservice2;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Microservice2Application {

    public static void main(String[] args) {
        SpringApplication.run(Microservice2Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    class Ms2TestController {

        private final RestTemplate restTemplate;

        public Ms2TestController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @GetMapping("/api/display")
        @CircuitBreaker(
                name = "microservice1",
                fallbackMethod = "getDefaultMessage"
        )
        public String display() {

            String ms1Response =
                    restTemplate.getForObject(
                            "http://localhost:9091/display",
                            String.class);

            return "Microservice 2 received: " + ms1Response;
        }

        public String getDefaultMessage(Exception ex) {
            return "This a default message when Microservice 1 is unavailable.";
        }
    }}