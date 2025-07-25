package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.dtos.UserResponse;
import com.hitta.ContractApp.model.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    @EntityGraph(attributePaths = {"drafts", "userForm","token", "verificationToken"})
    Optional<Users> findByEmail(String email);

    @Query("SELECT new com.hitta.ContractApp.dtos.UserResponse(u.id, u.name, u.email) FROM Users u WHERE u.id = :id")
    Optional<UserResponse> findUserInfoById(@Param("id") Long id);

    boolean existsByEmail(String email);
}