package com.mtb.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {
    private String search;
    private String role;
    private Boolean isActive;
    private LocalDate createdFrom;
    private LocalDate createdTo;
}
