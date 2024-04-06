package com.syrtin.dataprocessor.repository;

import com.syrtin.dataprocessor.model.Resume;
import com.syrtin.dataprocessor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByUser(User user);
}
