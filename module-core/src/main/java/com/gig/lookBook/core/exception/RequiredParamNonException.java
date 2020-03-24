package com.gig.lookBook.core.exception;

import lombok.NoArgsConstructor;

/**
 * 필수 파라메터 없음
 *
 * @author prographer
 * @date: 2019-04-19
 */
@NoArgsConstructor
public class RequiredParamNonException extends Exception {
    public RequiredParamNonException(String message) {
        super(message);
    }
}
