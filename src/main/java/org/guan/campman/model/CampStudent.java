package org.guan.campman.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "camp_student")
public class CampStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "student_id")
    private int studentId;
    @Column(name = "camp_id")
    private int campId;
    @Column(name = "exit_status")
    private boolean exitStatus;
    @Column(name = "exit_date")
    private LocalDate exitDate;
}
