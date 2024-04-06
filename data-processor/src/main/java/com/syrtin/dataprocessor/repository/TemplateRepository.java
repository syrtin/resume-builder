package com.syrtin.dataprocessor.repository;

import com.syrtin.dataprocessor.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
