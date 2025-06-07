package com.hitta.AuthTemplate.repo;

import com.hitta.AuthTemplate.dtos.UserResponse;
import com.hitta.AuthTemplate.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    @Query("SELECT u.id FROM Users u WHERE u.email = :email")
    Optional<Integer> findIdByEmail(@Param("email") String email);

    @Query("SELECT new com.hitta.AuthTemplate.dtos.UserResponse(u.id, u.name, u.email) FROM Users u WHERE u.id = :id")
    Optional<UserResponse> findUserInfoById(@Param("id") Integer id);

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.token " +
            "LEFT JOIN FETCH u.verificationToken " +
            "WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);

}
