package org.guan.campman.Repository;

import org.guan.campman.Model.CoachTeachNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachTeachNumRepo extends JpaRepository<CoachTeachNum, Integer> {
}
