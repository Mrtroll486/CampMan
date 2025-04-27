package org.guan.campman.Repository;

import org.guan.campman.Model.CoachGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachGroupRepo extends JpaRepository<CoachGroup, Integer> {
}
