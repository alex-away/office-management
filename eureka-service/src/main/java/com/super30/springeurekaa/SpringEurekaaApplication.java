package com.super30.springeurekaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringEurekaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEurekaaApplication.class, args);
    }

}
