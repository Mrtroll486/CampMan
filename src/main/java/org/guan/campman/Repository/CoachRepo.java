package org.guan.campman.Repository;

import org.guan.campman.Model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepo extends JpaRepository<Coach, Integer> {
    List<Coach> findByGroupId(int groupId);
    Optional<Coach> findFirstById(int id);
    @Query("SELECT r FROM Coach r JOIN CoachTeachNum c ON r.id = c.coachId ORDER BY c.num DESC")
    List<Coach> findAllOrderByTeachNum();
}
