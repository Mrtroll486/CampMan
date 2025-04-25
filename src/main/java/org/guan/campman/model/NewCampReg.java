package org.guan.campman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewCampReg {
    private String name;
    @JsonProperty("admin_id")
    private int adminId;
    private List<Coach> coaches;
    private List<Student> students;
    private int length;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("cost_per_stu")
    private int costPerStudent;
    @JsonProperty("refund_expr")
    private int refundExpr;
}
