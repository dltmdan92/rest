package com.seungmoo.springboot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * RestTemplate : Block I/O 기반의 Synchronous API
 * WebClient : Non-Blocking I/O 기반의 Asynchronous API (webflux가 의존성으로 들어와야 함)
 */
@Component
public class RestRunner implements ApplicationRunner {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private WebClient.Builder builder;

    Logger logger = LoggerFactory.getLogger(RestRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //RestTemplate restTemplate = restTemplateBuilder.build();
        WebClient webClient = builder
                                .baseUrl("http://localhost:8080")   // 지역적으로 커스터마이징 하는 방법
                                .build();

        // webClient 전역적으로 커스터마이징 했을 때는 이렇게 build() 만 해주면 된다.
        // WebClient webClient = builder.build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // TODO /hello
        // restTemplate은 blocking I/O이다. 즉 호출이 끝나기 전까지 아래 라인으로 넘어가지 않는다. (Synchronous)
        // String helloResult = restTemplate.getForObject("http://localhost:8080/hello", String.class);

        // webClient는 Non blocking I/O이다.
        Mono<String> helloMono = webClient.get().uri("/hello")
                                    .retrieve() // 결과값을 가져와라
                                    .bodyToMono(String.class); // mono타입으로 변경, 리액터 Mono에 대해 공부필요

        // Mono타입은 그자체로 동작하지 않음, subscribe(비동기, Non-blocking)를 통해 로직이 실행됨(http get 요청 보내서 response 가져오기 등등)
        helloMono.subscribe(s -> {
            logger.info(s);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
            logger.info(stopWatch.prettyPrint());
            stopWatch.start();
        });
        //logger.info(helloResult);

        // TODO /world
        //String worldResult = restTemplate.getForObject("http://localhost:8080/world", String.class);
        Mono<String> worldResult = webClient.get().uri("/world")
                                        .retrieve()
                                        .bodyToMono(String.class);
        worldResult.subscribe(s -> {
            logger.info(s);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
            logger.info(stopWatch.prettyPrint());
            stopWatch.start();
        });

        //logger.info(worldResult);
        //stopWatch.stop();
        //logger.info(stopWatch.prettyPrint());
    }
}
