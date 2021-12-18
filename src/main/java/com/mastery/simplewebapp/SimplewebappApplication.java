package com.mastery.simplewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimplewebappApplication {

    public static void main(String[] args) {
//        System.setProperty("spring.profiles.active", "test");
        SpringApplication.run(SimplewebappApplication.class, args);
    }

}
