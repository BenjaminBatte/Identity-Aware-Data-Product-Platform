package com.benjaminbatte.platform.common.exception;

public class ResourceNotFoundException extends PlatformException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
