package com.light.noteai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.light.noteai"
})
@MapperScan(value = {
        "com.light.noteai.dal.mapper"
})
public class NoteAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteAiApplication.class, args);
    }
}