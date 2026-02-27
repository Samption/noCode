package com.sxl.nocode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.sxl.nocode.mapper")
public class NoCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoCodeApplication.class, args);
    }

}
