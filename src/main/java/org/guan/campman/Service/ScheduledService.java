package org.guan.campman.Service;

import org.guan.campman.Model.Coach;
import org.guan.campman.Repository.CoachRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledService {
    private final RewardService rewardService;
    private final CoachRepo coachRepo;

    @Autowired
    public ScheduledService(RewardService rewardService, CoachRepo coachRepo) {
        this.rewardService = rewardService;
        this.coachRepo = coachRepo;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateAllCoachRewardFile() {
        List<Coach> coachList = coachRepo.findAll();
        LocalDate firstDayThisMonth = LocalDate.now();
        LocalDate firstDayLastMonth = firstDayThisMonth.minusMonths(1);
        for (Coach coach : coachList) {
            int id = coach.getId();
            rewardService.getCoachReward(id, firstDayLastMonth, firstDayThisMonth, true);
        }
    }
}
