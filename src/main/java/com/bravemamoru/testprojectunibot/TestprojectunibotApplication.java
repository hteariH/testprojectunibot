package com.bravemamoru.testprojectunibot;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.Random;

@SpringBootApplication
public class TestprojectunibotApplication {
    @Autowired
    StringGeneratorVerticle stringGeneratorVerticle;

    @Autowired
    NamesVerticle namesVerticle;

    public static void main(String[] args) {
        SpringApplication.run(TestprojectunibotApplication.class, args);
    }


    @PostConstruct
    void deployVerticles() {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(namesVerticle);
        vertx.deployVerticle(stringGeneratorVerticle);
    }

    @Bean
    Random getRandom() {
        return new Random();
    }
}
