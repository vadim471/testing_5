package com.example.demo.chuck;

import com.example.demo.config.ChuckProperties;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChuckServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    ChuckProperties chuckProperties;

    @Autowired
    ChuckClient chuckClient;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void returnJokeIfChuckResponseSuccess() {

        when(restTemplate.exchange(
                eq(chuckProperties.getUrl()),
                eq(HttpMethod.GET),
                isNull(),
                Mockito.<ParameterizedTypeReference<ChuckResponse>>any()))
                .thenReturn(ResponseEntity.ok(new ChuckResponse("test joke")));

        ChuckClient client = new ChuckClient(restTemplate, chuckProperties);

        ChuckResponse response = client.getJoke();

        assertEquals("test joke", response.getValue());
    }

    @Test
    public void returnDefaultJokeIfInternalServerError() {

        when(restTemplate.exchange(
                eq(chuckProperties.getUrl()),
                eq(HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<ChuckResponse>() {})))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Student newStudent = new Student("ivan", "r@mail.com", Gender.MALE);

        testRestTemplate.postForEntity("/api/v1/students", newStudent, void.class);
        String joke = testRestTemplate.getForObject("/api/v1/students/1", Student.class).getJoke();

        assertEquals("Random joke", joke);
    }

}
