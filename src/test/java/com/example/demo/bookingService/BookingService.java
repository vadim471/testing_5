package com.example.demo.bookingService;

import com.example.demo.config.BookingProperties;
import com.example.demo.config.ChuckProperties;
import com.example.demo.integration.BookingClient;
import com.example.demo.model.BookingResponse;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
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
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate testRestTemplate;


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    BookingClient bookingClient;

    @Autowired
    BookingProperties bookingProperties;

    @Autowired
    ChuckProperties chuckProperties;

    @Test
    public void checkBookingId() throws JsonProcessingException {
        int id = 1;
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Student createdStudent = new Student("ivan", "r@mail.com", Gender.MALE);

        var bookingRequest = new BookingResponse(
                id,
                new BookingResponse.Booking(
                        createdStudent.getName(),
                        "some-last-name",
                        new Random().nextInt(),
                        true,
                        new BookingResponse.BookingDates(
                                LocalDate.now(),
                                LocalDate.now().plusDays(7)
                        ),
                        "No needs"
                )
        );

        when(restTemplate.postForObject(eq(bookingProperties.getUrl() + "/booking"), any(), eq(BookingResponse.class)))
                .thenReturn(bookingRequest);

        when(restTemplate.exchange(
                eq(chuckProperties.getUrl()),
                eq(HttpMethod.GET),
                isNull(),
                Mockito.<ParameterizedTypeReference<ChuckResponse>>any()))
                .thenReturn(ResponseEntity.ok(new ChuckResponse("test joke")));

        testRestTemplate.postForEntity("/api/v1/students", createdStudent, void.class);
        int bookingId = testRestTemplate.getForObject("/api/v1/students/1", Student.class).getBookingId();

        assertEquals(id, bookingId);
    }

    @Test
    public void checkBookingIdIfServerError() {
        int expectedId = -1;

        BookingResponse bookingRequest = new BookingResponse();

        when(restTemplate.postForObject(eq(bookingProperties.getUrl() + "/booking"), any(), eq(BookingResponse.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        int extractId = bookingClient.createBooking(bookingRequest.toString());

        assertEquals(expectedId, extractId);

    }
}
