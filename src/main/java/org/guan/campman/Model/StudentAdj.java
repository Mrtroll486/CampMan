package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentAdj {
    @JsonProperty("is_exit")
    private boolean exitType;
    private String date;
    @JsonProperty("id")
    private int studentId;
    @JsonProperty("new_coach_id")
    private int newCoachId;
    @JsonProperty("camp_id")
    private int campId;
}
