package com.sxl.nocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NoCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoCodeApplication.class, args);
    }

}
