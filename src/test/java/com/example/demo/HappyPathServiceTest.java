package com.example.demo;

import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class HappyPathServiceTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void studentWireMockTest() throws JsonProcessingException {
        ChuckResponse chuckResponse = new ChuckResponse("some joke");
        stubFor(
                WireMock.
                        get("/jokes/random").
                        willReturn(
                                okJson(
                                        objectMapper.writeValueAsString(chuckResponse))));

        testRestTemplate.postForObject(
                "/api/v1/students",
                new Student("d", "d@f.ru", Gender.MALE), Void.class);

        System.out.println(
                testRestTemplate.getForObject("/api/v1/students/1", Student.class)
        );
    }
}
