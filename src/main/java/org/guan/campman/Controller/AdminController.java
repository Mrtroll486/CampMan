package org.guan.campman.Controller;

import org.guan.campman.Model.Admin;
import org.guan.campman.Model.AdminAdj;
import org.guan.campman.Model.ReturnData;
import org.guan.campman.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/api/getadmin")
    public List<Admin> getAdmin() {
        return adminService.getAdmins();
    }

    @PostMapping("/api/adminadj")
    public ReturnData adminAdj(@RequestBody AdminAdj adminAdj) {
        return adminService.adminAdj(adminAdj);
    }

    @PostMapping("/login")
    public ReturnData login(@RequestBody Admin admin) {
        return new ReturnData(0, "Vee!");
    }
}
