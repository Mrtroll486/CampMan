package org.guan.campman.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "coach_reward")
public class CoachReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "camp_id")
    private int campId;
    @Column(name = "coach_id")
    private int coachId;
    @Column(name = "student_id")
    private int studentId;
    private BigDecimal reward;

    public CoachReward() {}

    public CoachReward(int campId, int coachId, int studentId, BigDecimal reward) {
        this.campId = campId;
        this.coachId = coachId;
        this.studentId = studentId;
        this.reward = reward;
    }
}
