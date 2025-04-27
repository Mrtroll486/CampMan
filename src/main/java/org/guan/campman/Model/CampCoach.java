package org.guan.campman.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "camp_coach")
public class CampCoach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "coach_id")
    private int coachId;
    @Column(name = "camp_id")
    private int campId;
}
