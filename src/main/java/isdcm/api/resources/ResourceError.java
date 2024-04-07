package isdcm.api.resources;

import jakarta.ws.rs.core.Response.Status;

public enum ResourceError {
    VIDEO_NOT_FOUND(Status.NOT_FOUND, "VIDEO_NOT_FOUND"),
    FECHA_CREACION_MALFORMED(Status.BAD_REQUEST, "FECHA_CREACION_MALFORMED");
    
    final Status status;
    final String message;
    
    ResourceError(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}
