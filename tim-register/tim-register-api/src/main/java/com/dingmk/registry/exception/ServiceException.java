package com.dingmk.registry.exception;

/**
 * @author zt
 */
public abstract class ServiceException extends Exception {

    private static final long serialVersionUID = 2705349178540735568L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
