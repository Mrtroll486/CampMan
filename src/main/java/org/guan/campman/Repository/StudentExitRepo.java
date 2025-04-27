package org.guan.campman.Repository;

import org.guan.campman.Model.StudentExit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExitRepo extends JpaRepository<StudentExit, Integer> {
}
