package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, String> {

    Optional<VerificationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM VerificationToken vt WHERE vt.token = :token")
    void deleteByToken(@Param("token") String token);


}