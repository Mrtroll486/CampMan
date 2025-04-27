package org.guan.campman.Service;

import org.guan.campman.Exception.RollBackException;
import org.guan.campman.Model.Admin;
import org.guan.campman.Model.AdminAdj;
import org.guan.campman.Model.ReturnData;
import org.guan.campman.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepo adminRepo;

    @Autowired
    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public List<Admin> getAdmins() {
        return adminRepo.findAll();
    }

    @Transactional
    public ReturnData adminAdj(AdminAdj adj) {
        if (adj.isNewAdmin()) {
            // new admin register
            Admin newAdmin = new Admin(adj.getUserName(), adj.getUserKey(), adj.getRole());
            adminRepo.save(newAdmin);
        } else {
            // delete a admin account
            Admin target = adminRepo.findById(adj.getId()).orElseThrow(
                    () -> new RollBackException("No admin with such id")
            );
            target.setExited(true);
            adminRepo.save(target);
        }

        return new ReturnData(0, "success");
    }
}
