package com.example.mnp.dto;

import com.example.mnp.domain.PortingRequest;

import java.time.Instant;
import java.time.LocalDateTime;

public record PortingRequestResponseDto(Long id, String phoneNumber, String status, String recipient, String donor, Instant createdAt, Instant updatedAt, String rejectionReason) {
    public static PortingRequestResponseDto fromEntity(PortingRequest portingRequest){
        return new PortingRequestResponseDto(
                portingRequest.getId(),
                portingRequest.getPhoneNumber(),
                portingRequest.getStatus().name(),
                portingRequest.getRecipient().getName(),
                portingRequest.getDonor().getName(),
                portingRequest.getCreatedAt(),
                portingRequest.getUpdatedAt(),
                portingRequest.getRejectionReason()
        );
    }
}
