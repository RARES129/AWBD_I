package com.awbd.awbd.exceptions;

import lombok.Getter;

@Getter
public class EntityInUnfinishedAppointmentException extends RuntimeException {
    private final String entityName;

    public EntityInUnfinishedAppointmentException(String entityName) {
        super(entityName + " is used in an unfinished appointment.");
        this.entityName = entityName;
    }

}