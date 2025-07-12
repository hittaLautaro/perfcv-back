package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DraftRepo extends JpaRepository<Draft, Long> {

    Optional<List<Draft>> findAllByUserId(Long userId);
}
