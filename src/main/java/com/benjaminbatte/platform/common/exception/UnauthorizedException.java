package com.benjaminbatte.platform.common.exception;

public class UnauthorizedException extends PlatformException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
