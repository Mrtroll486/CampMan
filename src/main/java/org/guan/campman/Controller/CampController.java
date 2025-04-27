package org.guan.campman.Controller;

import org.guan.campman.Model.DetailedCamp;
import org.guan.campman.Model.PagedCamp;
import org.guan.campman.Model.ReturnData;
import org.guan.campman.Model.StudentAdj;
import org.guan.campman.Service.CampService;
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
    public ReturnData addCamp(@RequestBody DetailedCamp info) {
        return campService.addCamp(info);
    }

    @GetMapping("/api/hpinfo")
    public PagedCamp getHomePageInfo(@RequestParam("page_size") int pageSize,
                                     @RequestParam("page_index") int pageIndex ) {
        return campService.getPagedCoach(pageSize, pageIndex);
    }

    @GetMapping("/api/campdetails")
    public DetailedCamp getDetailedCamp(@RequestParam("id") int id) {
        return campService.getDetailedCamp(id);
    }

    @PostMapping("/api/stualt")
    public ReturnData studentAdj(@RequestBody StudentAdj adj) {
        return campService.stuAdjustment(adj);
    }
}
