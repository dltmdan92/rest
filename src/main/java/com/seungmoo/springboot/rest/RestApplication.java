package com.seungmoo.springboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @Bean
    public WebClientCustomizer webClientCustomizer() {
        // 전역적으로 커스터마이징 하는 방법
        return webClientBuilder -> webClientBuilder.baseUrl("http://localhost:8080");
    }
    
    // RestTemplate으로 http connect할 때, apache httpClient를 사용하는 방법
    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        // 더 이상 java.net의 httpConnection을 사용하지 않고 http client를 사용한다.
        return restTemplate -> restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}
