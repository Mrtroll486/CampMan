package org.guan.campman.service;

import org.guan.campman.model.*;
import org.guan.campman.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;

@Service
public class CampService {
    private final CampRepo campRepo;
    private final CoachRepo coachRepo;
    private final CampStudentRepo campStudentRepo;
    private final CampCoachRepo campCoachRepo;
    private final StudentRepo studentRepo;
    private final CoachStudentRepo coachStudentRepo;
    private final CoachTeachNumRepo coachTeachNumRepo;

    @Autowired
    public CampService(CampRepo campRepo, CoachRepo coachRepo, CampStudentRepo campStudentRepo,
                       CampCoachRepo campCoachRepo, StudentRepo studentRepo, CoachStudentRepo coachStudentRepo,
                       CoachTeachNumRepo coachTeachNumRepo) {
        this.campRepo = campRepo;
        this.coachRepo = coachRepo;
        this.campStudentRepo = campStudentRepo;
        this.campCoachRepo = campCoachRepo;
        this.studentRepo = studentRepo;
        this.coachStudentRepo = coachStudentRepo;
        this.coachTeachNumRepo = coachTeachNumRepo;
    }

    @Transactional
    public ReturnData addCamp(NewCampReg newCampReg) {
        // first init a camp to db
        Camp camp = new Camp();
        camp.setAdmin(newCampReg.getAdminId());
        camp.setCampName(newCampReg.getName());
        camp.setCampLength(newCampReg.getLength());
        camp.setStartDate(LocalDate.parse(newCampReg.getStartDate()));
        camp.setCostPerStu(newCampReg.getCostPerStudent());
        camp.setRefundExpr(newCampReg.getRefundExpr());
        campRepo.save(camp);
        int campId = camp.getId(); // here, after saving the data into the db, id field will be determined
        LocalDate startDate = camp.getStartDate();
        // update camp_coach
        List<Coach> coaches = newCampReg.getCoaches();
        Hashtable<Integer, CoachTeachNum> numStudents = new Hashtable<>();

        for(Coach coach : coaches) {
            CampCoach campCoach = new CampCoach();
            campCoach.setCampId(campId);
            campCoach.setCoachId(coach.getId());
            campCoachRepo.save(campCoach);
            CoachTeachNum num = coachTeachNumRepo.findById(coach.getId()).orElse(null);
            numStudents.put(campCoach.getCoachId(), num);
        }

        // update the coach_teach_num

        LocalDate today = camp.getStartDate();
        LocalDate endDate = today.plusDays(newCampReg.getLength());

        // update coach_student & camp_student
        List<Student> students = newCampReg.getStudents();
        for(Student student : students) {
            int studentId;
            List<Student> db_item = studentRepo.findByNameAndTel(student.getName(), student.getTel());
            // finding the right student ID
            if (!db_item.isEmpty()) {   // if there IS an item with identity name and tel
                studentId = db_item.get(0).getId();
            } else {
                studentRepo.save(student);
                studentId = student.getId();
            }

            // update coach_teach_num
            int temp = numStudents.get(student.getCoachId()).getNum();
            numStudents.get(student.getCoachId()).setNum(temp + 1);

            // update coach_student
            CoachStudent coachStudent = new CoachStudent();
            coachStudent.setCampId(campId);
            coachStudent.setCoachId(student.getCoachId());
            coachStudent.setStudentId(studentId);
            coachStudent.setStartDate(startDate);
            coachStudent.setHasChanged(false);
            coachStudent.setHasQuited(false);
            coachStudent.setEndDate(endDate);
            coachStudentRepo.save(coachStudent);

            // update camp_student
            CampStudent campStudent = new CampStudent();
            campStudent.setCampId(campId);
            campStudent.setStudentId(studentId);
            campStudent.setExitStatus(false);
            campStudent.setExitDate(null);
            campStudentRepo.save(campStudent);
        }

        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setContent("Success");
        return returnData;
    }

    public PagedCoach getPagedCoach(int pageSize, int pageIndex) {
        // Sort by start date by default
        Sort sort = Sort.by("startDate").descending();
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<Camp> camps = campRepo.findAll(pageable);
        return new PagedCoach(camps.getNumber(), camps.getTotalPages(), camps.getContent());
    }
}
