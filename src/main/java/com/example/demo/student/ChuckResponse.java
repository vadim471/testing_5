package com.example.demo.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChuckResponse {
    private String iconUrl;
    private String id;
    private String url;
    private String value;
}
