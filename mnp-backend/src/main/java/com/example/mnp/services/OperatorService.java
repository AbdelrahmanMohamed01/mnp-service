package com.example.mnp.services;

import com.example.mnp.exception.InvalidPhoneNumberException;
import com.example.mnp.repository.OperatorRepository;
import com.example.mnp.domain.Operator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {
    private final OperatorRepository operatorRepository;
    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository=operatorRepository;
    }

    public List<Operator> findAll() {
        return operatorRepository.findAll();
    }

    public Operator findByPhoneNumber(String phoneNumber){
        return operatorRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new InvalidPhoneNumberException("Phone number " + phoneNumber + " does not belong to any valid operator range."));
    }
}
