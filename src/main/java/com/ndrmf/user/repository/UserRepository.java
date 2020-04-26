package com.ndrmf.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndrmf.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByPasswordAndUsernameAndEnabledTrue(String password, String username);

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    List<User> findAllByEnabledTrue();

    List<User> findAllByEnabledFalse();
}
