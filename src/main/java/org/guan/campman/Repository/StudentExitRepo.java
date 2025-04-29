package org.guan.campman.Repository;

import org.guan.campman.Model.StudentExit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface StudentExitRepo extends JpaRepository<StudentExit, Integer> {
    List<StudentExit> findByStudentIdInAndCampIdInAndExitDateBetween
            (Collection<Integer> students, Collection<Integer> camps, LocalDate start, LocalDate end);
}
