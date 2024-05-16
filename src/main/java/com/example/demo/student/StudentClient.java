package com.example.demo.student;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentClient {
    private final RestTemplate restTemplate;
    private final StudentProperties properties;

    public StudentClient(RestTemplate restTemplate, StudentProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public ChuckResponse getJoke() {
        ResponseEntity<ChuckResponse> response = restTemplate.exchange(
                properties.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();

    }
}
