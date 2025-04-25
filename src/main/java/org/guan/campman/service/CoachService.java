package org.guan.campman.service;

import org.guan.campman.exception.RollBackException;
import org.guan.campman.model.*;
import org.guan.campman.repository.CoachGroupRepo;
import org.guan.campman.repository.CoachRepo;
import org.guan.campman.repository.CoachStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;

@Service
public class CoachService {
    private final CoachGroupRepo coachGroupRepo;
    private final CoachRepo coachRepo;
    private final CoachStudentRepo coachStudentRepo;

    @Autowired
    public CoachService(CoachGroupRepo coachGroupRepo, CoachRepo coachRepo, CoachStudentRepo coachStudentRepo) {
        this.coachGroupRepo = coachGroupRepo;
        this.coachRepo = coachRepo;
        this.coachStudentRepo = coachStudentRepo;
    }

    @Transactional
    public ReturnData updateCoaches(CoachInfoUpdate coachInfoUpdate) {
        List<CoachGroup> newGroups = coachInfoUpdate.getNewGroup();
        // use a mapper to convert front's temp coach-group mapping
        Hashtable<Integer, Integer> newGroupIdMapper = new Hashtable<>();

        // Init new groups
        if (newGroups != null) {
            for (CoachGroup group : newGroups) {
                int frontEndId = group.getId();
                // new init a instance to avoid ID collision
                CoachGroup newGroup = new CoachGroup();
                newGroup.setGroupName(group.getGroupName());
                coachGroupRepo.save(newGroup);
                int dbId = newGroup.getId();
                newGroupIdMapper.put(frontEndId, dbId);
            }
        }

        // Init new coaches
        List<Coach> newCoaches = coachInfoUpdate.getNewCoach();
        if (newCoaches != null) {
            for (Coach coach : newCoaches) {
                if (coach.getGroupId() < 0) {
                    coach.setGroupId(newGroupIdMapper.get(coach.getGroupId()));
                }
                coach.setExited(false);
                coach.setExitDate(null);
                coachRepo.save(coach);
            }
        }

        // Change EXISTING coaches' group
        List<Coach> changedCoaches = coachInfoUpdate.getUpdatedCoach();
        if (changedCoaches != null) {
            for (Coach coach : changedCoaches) {
                // note that these coach instances have nulls in their name and tel field
                int newGroupId = coach.getGroupIdUpd() < 0 ?
                        newGroupIdMapper.get(coach.getGroupIdUpd()) : coach.getGroupIdUpd();
                Coach coachItem = coachRepo.findById(coach.getId()).orElse(null);
                if (coachItem != null) {
                    coachItem.setGroupId(newGroupId);
                    coachRepo.save(coachItem);
                } else {
                    throw new RollBackException("No coach with such name while updating coach group");
                }
            }
        }

        // delete coach, first check whether this coach is teaching students
        // if so, throw exception and roll back
        List<Integer> deleteCoach = coachInfoUpdate.getRemoveCoach();
        LocalDate today = LocalDate.now();
        if (deleteCoach != null) {
            for (Integer id : deleteCoach) {
                // here use paging
                int pageIndex = 0;
                Page<CoachStudent> page;
                do {
                    Pageable pageable = PageRequest.of(pageIndex, 100);
                    page = coachStudentRepo.findByCoachId(id, pageable);
                    List<CoachStudent> coachStudents = page.getContent();

                    for (CoachStudent coachStudent : coachStudents) {
                        LocalDate endDate = coachStudent.getEndDate();
                        if (endDate.isAfter(today)) { // if this coach is currently teaching students
                            throw new RollBackException("End date is after today");
                        }
                    }
                    pageIndex++;
                } while (page.hasNext());
            }
        }

        // delete group, check whether this group have coaches in it
        // if so, throw exception and roll back
        List<Integer> deleteGroup = coachInfoUpdate.getRemoveGroup();
        if (deleteGroup != null) {
            for (Integer id : deleteGroup) {    // considering the group size, paging is unnecessary
                List<Coach> coachInGroup = coachRepo.findByGroupId(id);
                for(Coach coach : coachInGroup) {
                    if (!coach.isExited()) {
                        throw new RollBackException("There are still coaches in this group");
                    }
                }
            }
        }

        return new ReturnData(0, "Success");
    }
}
