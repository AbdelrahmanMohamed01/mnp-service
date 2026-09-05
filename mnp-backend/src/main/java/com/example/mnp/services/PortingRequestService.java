package com.example.mnp.services;

import com.example.mnp.dto.PortingDecisionDto;
import com.example.mnp.exception.AccessDeniedException;
import com.example.mnp.exception.InValidPortingStateException;
import com.example.mnp.exception.InvalidPortingRequestException;
import com.example.mnp.exception.ResourceNotFoundException;
import com.example.mnp.repository.PortingRequestRepository;
import com.example.mnp.domain.Operator;
import com.example.mnp.domain.PortingRequest;
import com.example.mnp.domain.PortingStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortingRequestService {
    private final PortingRequestRepository portingRequestRepository;
    private final OperatorService operatorService;
    public PortingRequestService(PortingRequestRepository portingRequestRepository,OperatorService operatorService) {
        this.portingRequestRepository=portingRequestRepository;
        this.operatorService=operatorService;
    }

    @Transactional
    public PortingRequest create(String phoneNumber, Operator recipient) {
        Operator donor=findCurrentDonor(phoneNumber);

        if(recipient.getId().equals(donor.getId())){
            throw new InvalidPortingRequestException("Phone number "+phoneNumber+ " is currently held by "+donor.getName()+", cannot port to the same operator.");
        }

        if(portingRequestRepository.existsByPhoneNumberAndStatus(phoneNumber,PortingStatus.PENDING)){
            PortingRequest portingRequest=new PortingRequest(phoneNumber, PortingStatus.REJECTED,recipient,donor);
            portingRequest.setRejectionReason("SYSTEM REJECTION: Phone number already has a pending porting request.");
            return portingRequestRepository.save(portingRequest);
        }

        PortingRequest portingRequest=new PortingRequest(phoneNumber, PortingStatus.PENDING,recipient,donor);
        return portingRequestRepository.save(portingRequest);
    }

    public List<PortingRequest>findAllByOperatorOrStatus(Operator currentOperator,PortingStatus portingStatus){
        return portingRequestRepository.findAllByOperatorOrStatus(currentOperator,portingStatus);
    }
    @Transactional
    public PortingRequest processDecision(Long id, PortingDecisionDto portingDecisionDto, Operator currentOperator)  {
        PortingStatus portingStatus=PortingStatus.from(portingDecisionDto.action());
        if(!PortingStatus.isValidDonorDecision(portingStatus)){
            throw new InValidPortingStateException("Action must be either 'ACCEPTED' or 'REJECTED'.");
        }

        PortingRequest portingRequest = portingRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porting request not found."));

        if(!portingRequest.getDonor().getId().equals(currentOperator.getId())){
            throw new AccessDeniedException("You are not the donor of the phone number, only the donor can process the request.");
        }
        if(!portingRequest.getStatus().equals(PortingStatus.PENDING)){
            throw new InValidPortingStateException("Cannot process decision for a request that is not PENDING.");
        }

        if (PortingStatus.REJECTED.equals(portingStatus)) {
            if (portingDecisionDto.rejectionReason() == null || portingDecisionDto.rejectionReason().isBlank()) {
                throw new InValidPortingStateException("A rejection reason must be provided when rejecting a porting request.");
            }
            portingRequest.setRejectionReason(portingDecisionDto.rejectionReason());
        }

        portingRequest.setStatus(portingStatus);
        return portingRequestRepository.save(portingRequest);
    }

    public Operator findCurrentDonor(String phoneNumber){
        Operator originalHolder=operatorService.findByPhoneNumber(phoneNumber);

        Optional<PortingRequest> lastAcceptedRequest=portingRequestRepository.findFirstByPhoneNumberAndStatusOrderByUpdatedAtDesc(phoneNumber, PortingStatus.ACCEPTED);
        if(lastAcceptedRequest.isPresent()){
            return lastAcceptedRequest.get().getRecipient();
        }

        return originalHolder;
    }
}
