package org.guan.campman.repository;

import org.guan.campman.model.StudentExit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExitRepo extends JpaRepository<StudentExit, Integer> {
}
