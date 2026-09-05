package com.example.mnp.dto;

import com.example.mnp.domain.Operator;

public record OperatorResponseDto(Long id, String name, String code) {
    public static OperatorResponseDto fromEntity(Operator operator){
        return new OperatorResponseDto(operator.getId(), operator.getName(),operator.getCode());
    }
}
