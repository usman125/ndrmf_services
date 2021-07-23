package com.ndrmf.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;




import com.ndrmf.user.model.TestTemp;


import java.util.UUID;

public interface TempTestRepository extends JpaRepository<TestTemp, UUID>{

}
