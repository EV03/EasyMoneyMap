package com.example.easymoneymapapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.easymoneymapapi.model.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
