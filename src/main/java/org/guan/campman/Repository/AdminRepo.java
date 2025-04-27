package org.guan.campman.Repository;

import org.guan.campman.Model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Page<Admin> findByUserName(String user_name, Pageable pageable);
}
