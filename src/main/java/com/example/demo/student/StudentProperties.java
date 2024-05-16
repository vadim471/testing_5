package com.example.demo.student;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chuck")
public class StudentProperties {

    @Getter
    @Setter
    private String url;
}
