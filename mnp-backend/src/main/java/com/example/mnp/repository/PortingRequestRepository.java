package com.example.mnp.repository;

import com.example.mnp.domain.Operator;
import com.example.mnp.domain.PortingRequest;
import com.example.mnp.domain.PortingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PortingRequestRepository extends JpaRepository<PortingRequest,Long> {
    @Query("select p from PortingRequest p join fetch p.recipient join fetch p.donor where p.recipient=:currentOperator or p.donor=:currentOperator or p.status=:status order by p.updatedAt desc")
    List<PortingRequest> findAllByOperatorOrStatus(@Param("currentOperator")Operator currentOperator,@Param("status") PortingStatus status);
    @EntityGraph(attributePaths = {"recipient","donor"})
    Optional<PortingRequest> findFirstByPhoneNumberAndStatusOrderByUpdatedAtDesc(String phoneNumber, PortingStatus status);
    Boolean existsByPhoneNumberAndStatus(String phoneNumber,PortingStatus status);
    @Modifying
    @Query("update PortingRequest p set p.status='CANCELLED', p.updatedAt=:now where p.status='PENDING' and p.createdAt<=:timeoutThreshold")
    int cancelTimedOutPendingRequests(@Param("timeoutThreshold") Instant timeoutThreshold, @Param("now") Instant now);
}
