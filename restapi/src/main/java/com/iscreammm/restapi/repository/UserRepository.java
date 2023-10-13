package com.iscreammm.restapi.repository;

import com.iscreammm.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByMail(String mail);
    List<User> findByUsernameContains(String username);
    User findByCode(String code);
}
