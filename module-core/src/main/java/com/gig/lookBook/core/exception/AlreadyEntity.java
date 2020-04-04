package com.gig.lookBook.core.exception;

import lombok.extern.java.Log;

/**
 * @author Jake
 * @date: 20-04-04
 */

@Log
public class AlreadyEntity extends Exception {
    public AlreadyEntity(String entityName) {
        super(entityName);
        log.info("alreadyEntity : " + entityName);
    }
}
