package org.guan.campman.Controller;

import org.guan.campman.exception.RollBackException;
import org.guan.campman.model.CoachInfoUpdate;
import org.guan.campman.model.ReturnData;
import org.guan.campman.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoachController {
    private final CoachService coachService;

    @Autowired
    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }
    @PostMapping("/api/updcoaches")
    public ReturnData updateCoaches(@RequestBody CoachInfoUpdate coachInfoUpdate) {
        try {
            return coachService.updateCoaches(coachInfoUpdate);
        } catch (RollBackException ex) {
            return new ReturnData(-1, ex.getContent());
        }
    }
}
