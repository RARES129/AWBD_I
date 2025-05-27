package com.awbd.awbd.exceptions;

import lombok.Getter;

@Getter
public class IllegalArgumentWithViewException extends RuntimeException {
    private final String attributeName;
    private final Object attributeValue;
    private final String viewName;

    public IllegalArgumentWithViewException(String message, String attributeName, Object attributeValue, String viewName) {
        super(message);
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.viewName = viewName;
    }
}
