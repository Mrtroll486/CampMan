package org.guan.campman.Controller;

import jakarta.transaction.Transactional;
import org.guan.campman.model.NewCampReg;
import org.guan.campman.model.PagedCoach;
import org.guan.campman.model.ReturnData;
import org.guan.campman.service.CampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CampController {
    private final CampService campService;

    @Autowired
    public CampController(CampService campService) {
        this.campService = campService;
    }

    @PostMapping("/api/newcamp")
    public ReturnData addCamp(@RequestBody NewCampReg info) {
        return campService.addCamp(info);
    }

    @GetMapping("/api/hpinfo")
    public PagedCoach getHomePageInfo(@RequestParam("page_size") int pageSize,
                                      @RequestParam("page_index") int pageIndex ) {
        return campService.getPagedCoach(pageSize, pageIndex);
    }
}
