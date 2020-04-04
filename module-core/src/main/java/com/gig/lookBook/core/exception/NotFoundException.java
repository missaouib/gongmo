package com.gig.lookBook.core.exception;

import lombok.extern.java.Log;

@Log
public class NotFoundException extends Exception {
    public static final int USER_NOT_FOUND = 100;
    public static final int USER_GROUP_NOT_FOUND = 200;
    public static final int ROLE_NOT_FOUND = 300;
    public static final int PRIVILEGE_NOT_FOUND = 301;
    public static final int BOARD_NOT_FOUND = 400;
    public static final int MENU_NOT_FOUND = 500;
    public static final int PARENT_MENU_NOT_FOUND = 501;
    public static final int CODE_NOT_FOUND = 600;
    public static final int GROUP_CODE_NOT_FOUND = 601;
    public static final int PRODUCT_NOT_FOUND = 700;
    public static final int ESTIMATE_NOT_FOUND = 800;

    private int code;

    public NotFoundException(String message, int code) {
        super(message);
        log.info("message : " + message);
        this.code = code;
    }

    public NotFoundException(int code) {
        super("Not Found Exception: " + code);
        this.code = code;
    }

    public NotFoundException(String product_not_found) {
        super(product_not_found);
    }
}
