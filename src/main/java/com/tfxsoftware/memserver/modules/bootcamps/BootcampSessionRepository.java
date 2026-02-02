package com.tfxsoftware.memserver.modules.bootcamps;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BootcampSessionRepository extends JpaRepository<BootcampSession, UUID>{ 
    
}
