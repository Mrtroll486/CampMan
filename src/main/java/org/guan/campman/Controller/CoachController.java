package org.guan.campman.Controller;

import org.guan.campman.Exception.RollBackException;
import org.guan.campman.Model.*;
import org.guan.campman.Service.CoachService;
import org.guan.campman.Service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@RestController
public class CoachController {
    private final CoachService coachService;
    private final RewardService rewardService;

    @Autowired
    public CoachController(CoachService coachService, RewardService rewardService) {
        this.coachService = coachService;
        this.rewardService = rewardService;
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

    @GetMapping("/api/coachreward")
    public RewardInfo getCoachReward(@RequestParam("id") int id, @RequestParam("start_date") LocalDate startDate,
                                     @RequestParam("end_date") LocalDate endDate) {
        return rewardService.getCoachReward(id, startDate, endDate, false);
    }

    @GetMapping("/api/detreward")
    public ResponseEntity<? extends Resource> getDetailedCoachReward(@RequestParam("file_name") String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource res = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType("text/csv")) // 设置成 CSV 类型！
                .contentLength(file.length())
                .body(res);
    }
}
