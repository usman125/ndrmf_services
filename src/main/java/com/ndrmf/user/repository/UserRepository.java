package com.ndrmf.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndrmf.user.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByPasswordAndUsernameAndEnabledTrue(String password, String username);

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    List<User> findAllByEnabledTrue();

    List<User> findAllByEnabledFalse();
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.enabled = true AND r.name = :roleName")
    List<User> findActiveUsersForRole(@Param("roleName") String roleName);
    
    @Query("SELECT u FROM User u JOIN u.org o WHERE o.id = :orgId AND u.enabled = true")
    List<User> findActiveUsersByOrganisation(@Param("orgId") int orgId);
    
    @Query("SELECT u.enabled FROM User u WHERE u.username = :username")
    boolean isEnabled(@Param("username") String username);
}
