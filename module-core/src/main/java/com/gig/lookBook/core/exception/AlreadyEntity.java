package com.gig.lookBook.core.exception;

/**
 * @author Jake
 * @date: 20-04-04
 */
public class AlreadyEntity extends Exception {
    public AlreadyEntity(String entityName) {
        super(entityName);
    }
}
