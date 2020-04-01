package com.gig.lookBook.core.exception;

import lombok.NoArgsConstructor;

/**
 * @author Jake
 * @date: 20/04/01
 */
@NoArgsConstructor
public class RequiredParamNonException extends Exception {
    public RequiredParamNonException(String message) {
        super(message);
    }
}
