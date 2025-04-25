package org.guan.campman.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "coach_student")
public class CoachStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "coach_id")
    private int coachId;

    @Column(name = "camp_id")
    private int campId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "has_changed")
    private boolean hasChanged;

    @Column(name = "has_quited")
    private boolean hasQuited;
}
