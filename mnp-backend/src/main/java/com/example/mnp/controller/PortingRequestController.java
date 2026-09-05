package com.example.mnp.controller;

import com.example.mnp.dto.CreatePortingRequestDto;
import com.example.mnp.dto.PortingDecisionDto;
import com.example.mnp.dto.PortingRequestResponseDto;
import com.example.mnp.domain.Operator;
import com.example.mnp.domain.PortingRequest;
import com.example.mnp.domain.PortingStatus;
import com.example.mnp.services.PortingRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/porting-requests")
public class PortingRequestController {
    private final PortingRequestService portingRequestService;
    public PortingRequestController(PortingRequestService portingRequestService){
        this.portingRequestService=portingRequestService;
    }
    @PostMapping
    public ResponseEntity<PortingRequestResponseDto>createPortingRequest(@Valid @RequestBody CreatePortingRequestDto createPortingRequestDto, @AuthenticationPrincipal Operator currentOperator){
        PortingRequest portingRequest=portingRequestService.create(createPortingRequestDto.phoneNumber(),currentOperator);
        PortingRequestResponseDto portingRequestResponse= PortingRequestResponseDto.fromEntity(portingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(portingRequestResponse);
    }
    @GetMapping
    public ResponseEntity<List<PortingRequestResponseDto>>getAllPortingRequests(@AuthenticationPrincipal Operator currentOperator){
        List<PortingRequest>portingRequests=portingRequestService.findAllByOperatorOrStatus(currentOperator,PortingStatus.ACCEPTED);
        List<PortingRequestResponseDto>portingRequestResponse=portingRequests.stream()
                .map(PortingRequestResponseDto::fromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(portingRequestResponse);
    }
    @PatchMapping("/{id}/decision")
    public ResponseEntity<PortingRequestResponseDto> processDecision(@PathVariable Long id, @Valid @RequestBody PortingDecisionDto decision, @AuthenticationPrincipal Operator currentOperator){
        PortingRequest portingRequest=portingRequestService.processDecision(id,decision,currentOperator);
        PortingRequestResponseDto portingRequestResponse=PortingRequestResponseDto.fromEntity(portingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(portingRequestResponse);
    }
}
