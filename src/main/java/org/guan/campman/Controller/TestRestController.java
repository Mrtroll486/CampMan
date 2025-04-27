package org.guan.campman.Controller;

import org.guan.campman.Model.Admin;
import org.guan.campman.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestRestController {
    @Autowired
    private AdminRepo adminRepo;

    @GetMapping("/admin")
    public Page<Admin> getAdmin() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Admin> admins = adminRepo.findByUserName("root", pageable).getContent();
        System.out.println(admins.size());
        for(Admin admin : admins) {
            System.out.println(admin.getId());
            System.out.println(admin.getUserName());
        }
        return adminRepo.findByUserName("root", pageable);
    }
}
