package com.manulife.id.exception;

import com.manulife.id.constant.Default;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class ProcessException  extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String source;
    private final String code;
    private final String note;

    public ProcessException(String message, String source, String code) {
        super(message);
        this.source = source;
        this.code = code;
        this.note = "";
    }

    public ProcessException(String message, String code) {
        super(message);
        this.source = Default.SOURCE;
        this.code = code;
        this.note = "";
    }


    public ProcessException(String message) {
        super(message);
        this.source = Default.SOURCE;
        this.code = "";
        this.note = "";
    }
}

