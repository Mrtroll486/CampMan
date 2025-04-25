package org.guan.campman.repository;

import org.guan.campman.model.CampCoach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampCoachRepo extends JpaRepository<CampCoach, Long> {
}
