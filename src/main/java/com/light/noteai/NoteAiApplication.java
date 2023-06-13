package com.light.noteai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.light.noteai"
})
@MapperScan(value = {
        "com.light.noteai.dal.mapper"
})
@EnableScheduling
public class NoteAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteAiApplication.class, args);
    }
}