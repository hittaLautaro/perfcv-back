package com.hitta.ContractApp.repo;

import com.hitta.ContractApp.model.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;import java.util.List;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {

    Page<Template> findByIsActiveTrue(Pageable pageable);

}
