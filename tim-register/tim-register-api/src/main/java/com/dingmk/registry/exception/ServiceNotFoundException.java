package com.dingmk.registry.exception;

/**
 * @author zt
 */
public class ServiceNotFoundException extends ServiceException {

    private static final long serialVersionUID = -1156101797299065567L;

    public ServiceNotFoundException() {
        super("service not found");
    }

    public ServiceNotFoundException(Throwable cause) {
        super("service not found", cause);
    }

}
