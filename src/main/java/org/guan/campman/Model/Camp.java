package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "camp")
public class Camp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int admin;
    @Column(name = "camp_name")
    @JsonProperty("camp_name")
    private String campName;
    @Column(name = "camp_length")
    @JsonProperty("camp_length")
    private int campLength;
    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDate startDate;
    @Column(name = "cost_per_stu")
    @JsonProperty("cost_per_stu")
    private int costPerStu;
    /** This is a flag member indicates the type of refund expr should use for this camp */
    @Column(name = "refund_expr")
    @JsonProperty("refund_expr")
    private int refundExpr;
}
