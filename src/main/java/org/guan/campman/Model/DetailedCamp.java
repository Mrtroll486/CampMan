package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/** This class used in 2 cases, one for new camp register, another for returning detailed camp info */
@Data
public class DetailedCamp {
    @JsonProperty("camp_name")
    private String campName;
    @JsonProperty("admin")
    private int admin;
    private List<Coach> coaches;
    private List<Student> students;
    @JsonProperty("camp_length")
    private int campLength;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("cost_per_stu")
    private int costPerStudent;
    @JsonProperty("refund_expr")
    private int refundExpr;

    public DetailedCamp(Camp camp, List<Coach> coaches, List<Student> students) {
        this.campName = camp.getCampName();
        this.admin = camp.getAdmin();
        this.coaches = coaches;
        this.students = students;
        this.campLength = camp.getCampLength();
        this.startDate = String.valueOf(camp.getStartDate());
        this.costPerStudent = camp.getCostPerStu();
        this.refundExpr = camp.getRefundExpr();
    }

    public DetailedCamp() {}
}
