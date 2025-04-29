package org.guan.campman.Repository;

import org.guan.campman.Model.CoachReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRewardRepo extends JpaRepository<CoachReward, Integer> {
    Optional<CoachReward> findFirstByCampIdAndCoachIdAndStudentId(Integer campId, Integer coachId, Integer studentId);
}
