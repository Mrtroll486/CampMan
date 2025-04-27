package org.guan.campman.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coach_teach_num")
public class CoachTeachNum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "coach_id")
    private int coachId;
    /** This int represent the number of students that this coach is teaching. */
    private int num;
}
