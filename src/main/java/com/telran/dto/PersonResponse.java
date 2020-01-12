package com.telran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime createdDate;
}
