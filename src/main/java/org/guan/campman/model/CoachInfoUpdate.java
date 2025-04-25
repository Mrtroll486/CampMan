package org.guan.campman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoachInfoUpdate {
    @JsonProperty("new_group")
    private List<CoachGroup> newGroup;
    @JsonProperty("new_coaches")
    private List<Coach> newCoach;
    @JsonProperty("update_coaches")
    private List<Coach> updatedCoach;
    @JsonProperty("remove_group")
    private List<Integer> removeGroup;
    @JsonProperty("remove_coach")
    private List<Integer> removeCoach;
}
