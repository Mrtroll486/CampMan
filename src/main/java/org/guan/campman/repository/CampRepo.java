package org.guan.campman.repository;

import lombok.NonNull;
import org.guan.campman.model.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampRepo extends JpaRepository<Camp, Long> {
    @NonNull
    Page<Camp> findAll(@NonNull Pageable pageable);
    // These 2 "@NonNull" just used to make sure that IDE shut up :)
}
