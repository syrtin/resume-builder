package com.syrtin.dataprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syrtin.dataprocessor.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
