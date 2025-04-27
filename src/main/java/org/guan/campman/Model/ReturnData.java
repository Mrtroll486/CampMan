package org.guan.campman.Model;

import lombok.Data;

@Data
public class ReturnData {
    private int code;
    private String content;

    public ReturnData() {}

    public ReturnData(int code, String content) {
        this.code = code;
        this.content = content;
    }
}
