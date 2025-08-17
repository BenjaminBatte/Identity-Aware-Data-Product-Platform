package com.benjaminbatte.platform.common.exception;

import lombok.Getter;

/**
 * Base class for all custom exceptions in the platform.
 */
@Getter
public abstract class PlatformException extends RuntimeException {
    private final String errorCode;

    protected PlatformException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
