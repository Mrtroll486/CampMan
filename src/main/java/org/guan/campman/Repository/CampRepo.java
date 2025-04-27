package org.guan.campman.Repository;

import lombok.NonNull;
import org.guan.campman.Model.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampRepo extends JpaRepository<Camp, Integer> {
    @NonNull
    Page<Camp> findAll(@NonNull Pageable pageable);
    // These 2 "@NonNull" just used to make sure that IDE shut up :)
}
