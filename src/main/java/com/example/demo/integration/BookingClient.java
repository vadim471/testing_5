package com.example.demo.integration;

import com.example.demo.config.BookingProperties;
import com.example.demo.model.BookingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class BookingClient {
    private final RestTemplate restTemplate;
    private final BookingProperties properties;

    private final ObjectMapper mapper;

    public int createBooking() {

        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        var bookingRequest = new BookingResponse.Booking("f", "d", 1, true, new BookingResponse.BookingDates(LocalDate.now(), LocalDate.now()), "d");

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BookingResponse.Booking> entity = new HttpEntity<>(bookingRequest, headers);
        var result = restTemplate.postForObject(properties.getUrl() + "/someNewMethod", entity, BookingResponse.class);

        return result.getBookingId();

    }
}
