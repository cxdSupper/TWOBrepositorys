package com.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyToolApplication.class, args);
    }
}