package com.example.mnp.controller;

import com.example.mnp.dto.PhoneNumberStatusResponseDto;
import com.example.mnp.services.PhoneNumberService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/phone-numbers")
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;
    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }
    @GetMapping("/{phoneNumber}/status")
    public ResponseEntity<PhoneNumberStatusResponseDto>getPhoneNumberStatus(
            @PathVariable
            @NotBlank(message = "Phone number is required.")
            @Pattern(regexp = "^01\\d{9}$", message = "Phone number must be an 11-digit string starting with '01'.")
            String phoneNumber
    ){
        PhoneNumberStatusResponseDto phoneNumberStatusResponseDto=phoneNumberService.getPhoneNumberStatus(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(phoneNumberStatusResponseDto);
    }
}
