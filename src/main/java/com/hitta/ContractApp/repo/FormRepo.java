package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepo extends JpaRepository<UserForm, Long> {
}
