package org.guan.campman.Controller;

import org.guan.campman.Exception.RollBackException;
import org.guan.campman.Model.Coach;
import org.guan.campman.Model.CoachGroup;
import org.guan.campman.Model.CoachInfoUpdate;
import org.guan.campman.Model.ReturnData;
import org.guan.campman.Service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/api/getgroup")
    public List<CoachGroup> getGroup() {
        return coachService.getGroups();
    }

    @GetMapping("/api/getcoach")
    public List<Coach> getCoach() {
        return coachService.getCoaches();
    }
}
