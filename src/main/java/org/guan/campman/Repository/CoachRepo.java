package org.guan.campman.Repository;

import org.guan.campman.Model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepo extends JpaRepository<Coach, Integer> {
    List<Coach> findByGroupId(int groupId);
}
