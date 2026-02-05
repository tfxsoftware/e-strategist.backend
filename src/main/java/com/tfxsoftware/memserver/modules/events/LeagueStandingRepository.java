package com.tfxsoftware.memserver.modules.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeagueStandingRepository extends JpaRepository<LeagueStanding, UUID> {
}
