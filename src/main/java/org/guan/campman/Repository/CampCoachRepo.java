package org.guan.campman.Repository;

import org.guan.campman.Model.CampCoach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampCoachRepo extends JpaRepository<CampCoach, Long> {
    List<CampCoach> findByCampId(int campId);
}
