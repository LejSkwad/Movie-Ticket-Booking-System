package com.mtb.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {
    private String search;
    private String role;
    private Boolean isActive;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdTo;
}
