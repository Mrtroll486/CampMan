package org.guan.campman.exception;

import lombok.Getter;

@Getter
public class RollBackException extends RuntimeException {
    private final String content;

    public RollBackException(String content) {
        super(content);
        this.content = content;
    }

}
