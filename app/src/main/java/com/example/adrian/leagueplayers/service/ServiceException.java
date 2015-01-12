package com.example.adrian.leagueplayers.service;

/**
 * Created by adrian on 1/8/15.
 */
public class ServiceException extends Exception {
    public ServiceException(String detailMessage) {
        super(detailMessage);
    }

    public ServiceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
