package com.example.demo;

import com.example.demo.student.StudentController;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8081)
public class StudentWireMockTest {

    @Value("${chuck.url}")
    private String chuckUrl;

    @Autowired
    private StudentController controller;

    @Test
    public void foo() {
        String path = null;
        try {
            path = new URL(chuckUrl).getPath();
        } catch (MalformedURLException ex) {
            System.out.println("Invalid URL");
        }

        stubFor(WireMock.get(path).willReturn(ok().withHeader("Content-Type", "application/json").withBody("""
                {
                    "categories": [],
                    "created_at": "2020-01-05 13:42:26.194739",
                    "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                    "id": "ZYjOeEY_SbalOv5bmGAapQ",
                    "updated_at": "2020-01-05 13:42:26.194739",
                    "url": "https://api.chucknorris.io/jokes/ZYjOeEY_SbalOv5bmGAapQ",
                    "value": "Chuck Norris' first car was a Caterpillar D9."
                }
                """)));

        controller.getRandomJoke();

        verify(getRequestedFor(urlEqualTo(path)));
    }
}
