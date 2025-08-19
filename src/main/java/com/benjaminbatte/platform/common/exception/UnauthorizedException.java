// UnauthorizedException.java
package com.benjaminbatte.platform.common.exception;

public class UnauthorizedException extends PlatformException {
    public static final String CODE = "UNAUTHORIZED";

    /** 401 with a generic message */
    public UnauthorizedException() { super("Authentication required", CODE); }

    /** 401 with a custom message */
    public UnauthorizedException(String message) { super(message, CODE); }

    /** 401 with message + cause */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, CODE, cause);
    }
}
