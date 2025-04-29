package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RewardInfo {
    private BigDecimal total;
    private BigDecimal refund;
    @JsonProperty("file_name")
    private String fileName;

    public RewardInfo(BigDecimal total, BigDecimal refund, String fileName) {
        this.total = total;
        this.refund = refund;
        this.fileName = fileName;
    }

    public RewardInfo() {}
}
