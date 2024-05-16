package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StudentClientTest {
    @Autowired
    private StudentClient studentClient;

    @Test
    void getJoke() {
        System.out.println(studentClient.getJoke());
    }
}