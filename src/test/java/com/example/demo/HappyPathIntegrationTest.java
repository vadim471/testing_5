package com.example.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HappyPathIntegrationTest {

    @Test
    public void test() {
        Response response = RestAssured.given()
                .auth()
                .none()
                .baseUri("http://localhost:8080")
                .basePath("/api/v1/students/1")
                .get()
                .then()
                .statusCode(509)
                .extract().response();
        String message = response.getBody().path("message").toString();
        assertEquals("dsa", message);
    }
}
