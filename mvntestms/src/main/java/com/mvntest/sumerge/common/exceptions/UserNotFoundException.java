package com.mvntest.sumerge.common.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7459962131571611029L;

    public UserNotFoundException() {
        super("User is not found");
    }
}
