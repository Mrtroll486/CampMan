package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminAdj {
    @JsonProperty("new_admin")
    private boolean newAdmin;
    private int id;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_key")
    private String userKey;
    private int role;
}
