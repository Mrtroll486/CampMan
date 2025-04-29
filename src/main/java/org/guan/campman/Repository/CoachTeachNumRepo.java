package org.guan.campman.Repository;

import org.guan.campman.Model.CoachTeachNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachTeachNumRepo extends JpaRepository<CoachTeachNum, Integer> {
    Optional<CoachTeachNum> findFirstByCoachId(int id);
}
