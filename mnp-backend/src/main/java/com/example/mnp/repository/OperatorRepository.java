package com.example.mnp.repository;

import com.example.mnp.domain.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator,Long> {
    @Query("select o from Operator o where o.prefixStart<=:phoneNumber and o.prefixEnd>=:phoneNumber")
    Optional<Operator> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    Optional<Operator> findByCode(String code);
}
