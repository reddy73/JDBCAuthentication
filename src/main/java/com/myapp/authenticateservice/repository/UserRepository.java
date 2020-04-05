package com.myapp.authenticateservice.repository;

import com.myapp.authenticateservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserId(String userId);
}
