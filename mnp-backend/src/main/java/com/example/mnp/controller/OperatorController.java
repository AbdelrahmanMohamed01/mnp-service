package com.example.mnp.controller;

import com.example.mnp.dto.OperatorResponseDto;
import com.example.mnp.domain.Operator;
import com.example.mnp.services.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
    private final OperatorService operatorService;
    public OperatorController(OperatorService operatorService) {
        this.operatorService=operatorService;
    }
    @GetMapping
    public ResponseEntity<List<OperatorResponseDto>>getAllOperators(){
        List<Operator> operators=operatorService.findAll();
        List<OperatorResponseDto>operatorsResponse=operators.stream()
                .map(OperatorResponseDto::fromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(operatorsResponse);
    }
}
