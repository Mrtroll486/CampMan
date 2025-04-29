package org.guan.campman.Service;

import org.guan.campman.Exception.RollBackException;
import org.guan.campman.Model.*;
import org.guan.campman.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
public class CampService {
    private final CampRepo campRepo;
    private final CoachRepo coachRepo;
    private final CampStudentRepo campStudentRepo;
    private final CampCoachRepo campCoachRepo;
    private final StudentRepo studentRepo;
    private final CoachStudentRepo coachStudentRepo;
    private final CoachTeachNumRepo coachTeachNumRepo;
    private final StudentExitRepo studentExitRepo;

    @Autowired
    public CampService(CampRepo campRepo, CoachRepo coachRepo, CampStudentRepo campStudentRepo,
                       CampCoachRepo campCoachRepo, StudentRepo studentRepo, CoachStudentRepo coachStudentRepo,
                       CoachTeachNumRepo coachTeachNumRepo, StudentExitRepo studentExitRepo) {
        this.campRepo = campRepo;
        this.coachRepo = coachRepo;
        this.campStudentRepo = campStudentRepo;
        this.campCoachRepo = campCoachRepo;
        this.studentRepo = studentRepo;
        this.coachStudentRepo = coachStudentRepo;
        this.coachTeachNumRepo = coachTeachNumRepo;
        this.studentExitRepo = studentExitRepo;
    }

    @Transactional
    public ReturnData addCamp(DetailedCamp newCampReg) {
        // first init a camp to db
        Camp camp = new Camp();
        camp.setAdmin(newCampReg.getAdmin());
        camp.setCampName(newCampReg.getCampName());
        camp.setCampLength(newCampReg.getCampLength());
        camp.setStartDate(LocalDate.parse(newCampReg.getStartDate()));
        camp.setCostPerStu(newCampReg.getCostPerStudent());
        camp.setRefundExpr(newCampReg.getRefundExpr());
        campRepo.save(camp);
        int campId = camp.getId(); // here, after saving the data into the db, id field will be determined
        LocalDate startDate = camp.getStartDate();
        // update camp_coach
        List<Coach> coaches = newCampReg.getCoaches();
        // here, because the CoachTeachNum is queried from DB then these instances are automatically managed
        // that is, you don't need to manually save it.
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
        LocalDate endDate = today.plusDays(newCampReg.getCampLength());

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

    public PagedCamp getPagedCoach(int pageSize, int pageIndex) {
        // Sort by start date by default
        Sort sort = Sort.by("startDate").descending();
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<Camp> camps = campRepo.findAll(pageable);
        return new PagedCamp(camps.getNumber(), camps.getTotalPages(), camps.getContent());
    }

    public DetailedCamp getDetailedCamp(int campId) {
        Camp targetCamp = campRepo.findById(campId).orElseThrow(
                () -> new RollBackException("Camp with this id doesn't exist"));
        // if every thing goes well, targetCamp won't be null
        List<Coach> coaches = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<CampCoach> campCoaches = campCoachRepo.findByCampId(campId);
        for (CampCoach campCoach : campCoaches) {
            int coachId = campCoach.getCoachId();
            coachRepo.findById(coachId).ifPresent(coaches::add);
        }
        List<CampStudent> campStudents = campStudentRepo.findByCampId(campId);
        for (CampStudent campStudent : campStudents) {
            if (!campStudent.isExitStatus()) {
                int studentId = campStudent.getStudentId();
                studentRepo.findById(studentId).ifPresent(students::add);
            }
        }
        return new DetailedCamp(targetCamp, coaches, students);
    }

    @Transactional
    public ReturnData stuAdjustment(StudentAdj adj) {
        int studentId = adj.getStudentId(), campId = adj.getCampId();
        if (adj.isExitType()) { // this adjustment is exiting, update camp_student, coach_student and student_exit
            // updating camp_student table
            List<CampStudent> campStudents = campStudentRepo.findByStudentIdAndCampId(studentId, campId);
            if (!campStudents.isEmpty()) {
                CampStudent campStudent = campStudents.get(0);
                campStudent.setExitStatus(true);
                campStudent.setExitDate(LocalDate.parse(adj.getDate()));
                campStudentRepo.save(campStudent);
            }

            // updating coach_student
            Optional<CoachStudent> coachStudents =
                    coachStudentRepo.findFirstByStudentIdAndCampIdOrderByStartDateDesc(studentId, campId);
            CoachStudent coachStudent = coachStudents.orElseThrow(
                    () -> new RollBackException("No student with such studentId or no camp with such campId"));
            coachStudent.setEndDate(LocalDate.parse(adj.getDate()));
            coachStudent.setHasQuited(true);
            coachStudentRepo.save(coachStudent);

            // updating coach_teach_num
            int coachId = coachStudent.getCoachId();
            CoachTeachNum num = coachTeachNumRepo.findFirstByCoachId(coachId).orElseThrow(
                    () -> new RollBackException("Item with such coachId doesn't exist"));
            num.setNum(num.getNum() - 1);
            coachTeachNumRepo.save(num);

            // updating student_exit
            StudentExit item = new StudentExit();
            item.setCampId(campId);
            item.setStudentId(studentId);
            item.setExitDate(LocalDate.parse(adj.getDate()));
            item.setExitReason("Nothing special");
            studentExitRepo.save(item);

            return new ReturnData(0, "success");
        } else {
            // change coach, update coach_student table
            Camp camp = campRepo.findById(campId).orElseThrow(
                    () -> new RollBackException("Camp with this id doesn't exist"));
            CoachStudent coachStudent =
                    coachStudentRepo.findFirstByStudentIdAndCampIdOrderByStartDateDesc(studentId, campId).orElseThrow(
                            () -> new RollBackException("No student with such student Id"));
            coachStudent.setEndDate(LocalDate.parse(adj.getDate()));
            coachStudent.setHasChanged(true);
            coachStudentRepo.save(coachStudent);
            CoachStudent newCoachStudent = new CoachStudent(adj.getNewCoachId(), campId, studentId,
                    LocalDate.parse(adj.getDate()), camp.getStartDate().plusDays(camp.getCampLength()));
            coachStudentRepo.save(newCoachStudent);

            CoachTeachNum oldNum = coachTeachNumRepo.findFirstByCoachId(coachStudent.getCoachId()).orElseThrow(
                    () -> new RollBackException("Item with such coachId doesn't exist"));
            oldNum.setNum(oldNum.getNum() - 1);
            CoachTeachNum newNum = coachTeachNumRepo.findFirstByCoachId(adj.getNewCoachId()).orElseThrow(
                    () -> new RollBackException("Item with such coachId doesn't exist"));
            newNum.setNum(newNum.getNum() + 1);

            return new ReturnData(0, "success");
        }
    }
}
