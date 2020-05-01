package com.ndrmf.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndrmf.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    
    @Query(value = "SELECT r FROM Role r JOIN r.org o WHERE o.id = :orgId")
    List<Role> findRolesByOrg(@Param("orgId") int orgId); 
}
