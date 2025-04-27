package org.guan.campman.Repository;

import org.guan.campman.Model.CampStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampStudentRepo extends JpaRepository<CampStudent, Integer> {
    List<CampStudent> findByCampId(Integer campId);
    List<CampStudent> findByStudentIdAndCampId(Integer studentId, Integer campId);
}
