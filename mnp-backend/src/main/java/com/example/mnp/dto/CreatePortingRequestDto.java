package com.example.mnp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreatePortingRequestDto(
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^01\\d{9}$", message = "Phone number must be an 11-digit string starting with 01")
        String phoneNumber
) {

}
