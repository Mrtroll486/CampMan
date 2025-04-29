package org.guan.campman.Service;

import com.opencsv.CSVWriter;
import org.guan.campman.Exception.RollBackException;
import org.guan.campman.Model.*;
import org.guan.campman.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RewardService {
    private final CoachRepo coachRepo;
    private final CoachStudentRepo coachStudentRepo;
    private final CampRepo campRepo;
    private final StudentRepo studentRepo;
    private final CoachRewardRepo coachRewardRepo;
    private final StudentExitRepo studentExitRepo;

    @Autowired
    public RewardService(CoachRepo coachRepo, CoachStudentRepo coachStudentRepo, CampRepo campRepo,
                         StudentRepo studentRepo, CoachRewardRepo coachRewardRepo, StudentExitRepo studentExitRepo) {
        this.coachRepo = coachRepo;
        this.coachStudentRepo = coachStudentRepo;
        this.campRepo = campRepo;
        this.studentRepo = studentRepo;
        this.coachRewardRepo = coachRewardRepo;
        this.studentExitRepo = studentExitRepo;
    }

    private BigDecimal getRefund(int exprType, BigDecimal reward, int len, int campLen) {
        switch (exprType) {
            case 0: {   // no matter when the refund request is issued, refund all of them
                return reward;
            }
            case 1: {   // determine the refund amount by the value of (actual learning days / total camp length)
                double percent = (double) len / (double) campLen;
                if (percent < 0.33) {
                    return reward.multiply(BigDecimal.valueOf(0.8)).setScale(2, RoundingMode.HALF_UP);
                } else {
                    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                }
            }
            // more expr can be added.
            default: {
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
        }

    }

    @Transactional
    public RewardInfo getCoachReward(int id, LocalDate startDate, LocalDate endDate, boolean isRoutine) {
        // make sure path exists
        String dirPath = "rewardLog";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Cannot create directory " + dirPath);
            }
        }
        String coachName = coachRepo.findFirstById(id).orElseThrow(
                () -> new RollBackException("No coach with such id")
        ).getName();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String csvPath = dirPath + "/" + (isRoutine ? "routine_" : "") + coachName + "-" + startDate.toString() + "_"
                + endDate.toString() + "_" + LocalDateTime.now().format(format) + ".csv";
        ArrayList<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"camp", "student", "costPerDay", "numDays", "total"});

        // calculate all rewards, which means that the record's interval overlaps with given interval
        List<CoachStudent> targets = coachStudentRepo.findByCoachIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                id, endDate, startDate);
        // here costMapper stores the cost of one day
        Hashtable<Integer, BigDecimal> costMapper = new Hashtable<>();
        BigDecimal sum = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        HashSet<Integer> studentIds = new HashSet<>(), campIds = new HashSet<>();
        for (CoachStudent target : targets) {
            int campId = target.getCampId(), studentId = target.getStudentId();
            campIds.add(campId);
            studentIds.add(studentId);
            String campName = campRepo.findById(campId).orElseThrow(
                    () -> new RollBackException("No camp with such id")).getCampName();
            String studentName = studentRepo.findById(studentId).orElseThrow(
                    () -> new RollBackException("No student with such id")).getName();
            BigDecimal cost;
            // get the cost and expr;
            if (!costMapper.containsKey(campId)) {
                Camp targetCamp = campRepo.findById(campId).orElseThrow(
                        () -> new RollBackException("No camp with such campId")
                );
                BigDecimal length = BigDecimal.valueOf(targetCamp.getCampLength());
                BigDecimal totalCost = BigDecimal.valueOf(targetCamp.getCostPerStu());
                cost = totalCost.divide(length, 2, RoundingMode.HALF_UP);
                costMapper.put(campId, cost);
            } else {
                cost = costMapper.get(campId);
            }

            LocalDate intervalStart = startDate.isAfter(target.getStartDate()) ? startDate : target.getStartDate();
            LocalDate intervalEnd = endDate.isBefore(target.getEndDate()) ? endDate : target.getEndDate();
            BigDecimal intervalLen = BigDecimal.valueOf(Math.abs(ChronoUnit.DAYS.between(intervalStart, intervalEnd)));

            BigDecimal reward = intervalLen.multiply(cost).setScale(2, RoundingMode.HALF_UP);

            sum = sum.add(reward).setScale(2, RoundingMode.HALF_UP);
            csvData.add(new String[]{campName, studentName, String.valueOf(cost),
                    String.valueOf(intervalLen), String.valueOf(reward)});

            if (isRoutine) {    // if this is a routine task, update DB
                Optional<CoachReward> item =
                        coachRewardRepo.findFirstByCampIdAndCoachIdAndStudentId(campId, id, studentId);
                if (item.isPresent()) {     // DB have an item in it.
                    CoachReward unpackedItem = item.get();
                    unpackedItem.setReward(unpackedItem.getReward().add(reward));
                    coachRewardRepo.save(unpackedItem);
                } else {
                    CoachReward newItem = new CoachReward(campId, id, studentId, reward);
                    coachRewardRepo.save(newItem);
                }
            }
        }

        if (isRoutine) {        // only calculate the refund in routine task
            List<StudentExit> exitTargets = studentExitRepo.findByStudentIdInAndCampIdInAndExitDateBetween
                    (studentIds, campIds, startDate, endDate);
            for (StudentExit target : exitTargets) {
                int campId = target.getCampId(), studentId = target.getStudentId();
                Optional<CoachReward> optionalCoachReward =
                        coachRewardRepo.findFirstByCampIdAndCoachIdAndStudentId(campId, id, studentId);
                if (optionalCoachReward.isPresent()) {      // only when DB have a record.
                    String campName = campRepo.findById(campId).orElseThrow(
                            () -> new RollBackException("No camp with such id")).getCampName();
                    String studentName = studentRepo.findById(studentId).orElseThrow(
                            () -> new RollBackException("No student with such id")).getName();
                    CoachReward coachReward = optionalCoachReward.get();
                    Camp targetCamp = campRepo.findById(campId).orElseThrow(
                            () -> new RollBackException("No camp with such Id")
                    );

                    int refundExpr = targetCamp.getRefundExpr();

                    // note that here we calculate the TOTAL learning days of a student in a camp,
                    // not specifying the coach
                    List<CoachStudent> targetCoachStudents = coachStudentRepo
                            .findByCampIdAndStudentIdOrderByStartDateAsc(campId, studentId);
                    int days = 0;
                    for (CoachStudent coachStudent : targetCoachStudents) {
                        days += (int) Math.abs(
                                ChronoUnit.DAYS.between(coachStudent.getStartDate(), coachStudent.getEndDate())
                        );
                    }
                    BigDecimal refund = getRefund(refundExpr, coachReward.getReward(), days, targetCamp.getCampLength());
                    sum = sum.subtract(refund).setScale(2, RoundingMode.HALF_UP);
                    coachReward.setReward(coachReward.getReward().subtract(refund).setScale(2, RoundingMode.HALF_UP));
                    coachRewardRepo.save(coachReward);

                    csvData.add(new String[]{campName, studentName, "refund", "refund", String.valueOf(refund)});
                }
            }
        }

        // writing csv
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            for (String[] csvRow : csvData) {
                writer.writeNext(csvRow);
            }
        } catch (IOException e) {
            throw new RollBackException("Cannot write to file " + csvPath + "due to " + e.getMessage());
        }

        return new RewardInfo(sum, BigDecimal.valueOf(0), csvPath);
    }
}
