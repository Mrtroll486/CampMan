package org.guan.campman.repository;

import org.guan.campman.model.CoachStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachStudentRepo extends JpaRepository<CoachStudent, Integer> {
    Page<CoachStudent> findByCoachId(int coachId, Pageable pageable);
}
