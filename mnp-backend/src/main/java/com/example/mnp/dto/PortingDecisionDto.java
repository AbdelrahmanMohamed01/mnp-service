package com.example.mnp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PortingDecisionDto(
        @NotBlank(message = "Action is required.")
        String action,
        @Size(max = 255, message = "Rejection reason cannot exceed 255 characters.")
        String rejectionReason
) {

}
