package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DraftRepo extends JpaRepository<Draft, Long> {

    @Query("SELECT d FROM Draft d WHERE d.user.id = :userId")
    Optional<List<Draft>> findAllByUserId(@Param("userId") Long userId);


    void deleteByUserIdAndId(Long userId, Long id);
}
