package org.guan.campman.Repository;

import org.guan.campman.Model.CoachStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachStudentRepo extends JpaRepository<CoachStudent, Integer> {
    Page<CoachStudent> findByCoachId(int coachId, Pageable pageable);
    Optional<CoachStudent> findFirstByStudentIdAndCampIdOrderByStartDateDesc(int studentId, int campId);
    List<CoachStudent> findByStudentIdAndCampIdOrderByStartDateAsc(int studentId, int campId);
}
