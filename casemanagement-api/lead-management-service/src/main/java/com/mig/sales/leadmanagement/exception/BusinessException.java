package com.mig.sales.leadmanagement.exception;

/**
 * Exception thrown when a business rule is violated
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

