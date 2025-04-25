package org.guan.campman.repository;

import org.guan.campman.model.CampStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampStudentRepo extends JpaRepository<CampStudent, Integer> {
}
