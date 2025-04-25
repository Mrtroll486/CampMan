package org.guan.campman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    @JsonProperty("user_name")
    private String userName;
    @Column(name = "user_key")
    @JsonProperty("user_key")
    private String userKey;
    /** This is a special flag for admin roles, 0 for normal, 1 for super */
    private int role;
    @Column(name = "is_exit")
    @JsonProperty("is_exit")
    private boolean exited;
}
