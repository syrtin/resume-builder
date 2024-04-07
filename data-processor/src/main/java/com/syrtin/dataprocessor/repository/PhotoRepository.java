package com.syrtin.dataprocessor.repository;

import com.syrtin.dataprocessor.model.Photo;
import com.syrtin.dataprocessor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByUser (User user);
}