package com.gig.lookBook.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResultDto<T> {

    final public static int RESULT_CODE_OK = 200;
    final public static int RESULT_CODE_NOT_FOUND = 404;
    final public static int RESULT_CODE_ERROR = 500;
    final public static int RESULT_CODE_INVALID = 501;
    final public static int RESULT_CODE_ALREADY = 502;

    final public static int RESULT_CODE_NOT_FOUND_PARAMETER = 601;
    final public static int RESULT_CODE_NO_DATA = 602;

    private int code;
    private String message;
    private T data;

    public ApiResultDto(int code) {
        this.code = code;
    }
}
