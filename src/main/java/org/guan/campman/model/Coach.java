package org.guan.campman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "coach")
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String tel;
    @JsonProperty("group_id")
    @Column(name = "group_id")
    private int groupId;
    @Column(name = "exit_status")
    private boolean exited;
    @Column(name = "exit_date")
    private LocalDate exitDate;
    @Transient
    @JsonProperty("new_group_id")
    /* This field should only be filled and used when updating the coach's group */
    private int groupIdUpd;
}
