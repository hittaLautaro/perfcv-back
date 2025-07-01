package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {


}
