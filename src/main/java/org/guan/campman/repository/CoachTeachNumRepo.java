package org.guan.campman.repository;

import org.guan.campman.model.CoachTeachNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachTeachNumRepo extends JpaRepository<CoachTeachNum, Integer> {
}
