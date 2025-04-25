package org.guan.campman.repository;

import org.guan.campman.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepo extends JpaRepository<Coach, Integer> {
    List<Coach> findByGroupId(int groupId);
}
