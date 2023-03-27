package com.iscreammm.restapi.repository;

import com.iscreammm.restapi.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {
    Gender findByGender(String gender);
}
