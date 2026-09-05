package com.example.mnp.services;

import com.example.mnp.dto.PhoneNumberStatusResponseDto;
import com.example.mnp.repository.PortingRequestRepository;
import com.example.mnp.domain.Operator;
import com.example.mnp.domain.PortingRequest;
import com.example.mnp.domain.PortingStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneNumberService {
    private final PortingRequestRepository portingRequestRepository;
    private final OperatorService operatorService;
    public PhoneNumberService(PortingRequestRepository portingRequestRepository, OperatorService operatorService){
        this.portingRequestRepository=portingRequestRepository;
        this.operatorService=operatorService;
    }
    public PhoneNumberStatusResponseDto getPhoneNumberStatus(String phoneNumber){
        Operator originalHolder=operatorService.findByPhoneNumber(phoneNumber);

        Optional<PortingRequest> lastAcceptedRequest=portingRequestRepository.findFirstByPhoneNumberAndStatusOrderByUpdatedAtDesc(phoneNumber, PortingStatus.ACCEPTED);
        if(lastAcceptedRequest.isPresent()){
            return new PhoneNumberStatusResponseDto(phoneNumber,lastAcceptedRequest.get().getStatus().name(),true,lastAcceptedRequest.get().getRecipient().getName());
        }

        return new PhoneNumberStatusResponseDto(phoneNumber,null,false,originalHolder.getName());
    }
}
