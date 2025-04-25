package org.guan.campman.repository;

import org.guan.campman.model.CoachGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachGroupRepo extends JpaRepository<CoachGroup, Integer> {
}
