package org.guan.campman.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "student_exit")
public class StudentExit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "camp_id")
    private int campId;

    @Column(name = "exit_date")
    private LocalDate exitDate;

    @Column(name = "exit_reason")
    private String exitReason;
}
