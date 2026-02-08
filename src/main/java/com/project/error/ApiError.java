package com.project.error;

import java.time.Instant;
import java.util.List;

public class ApiError {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<FieldErrorDTO> fieldErrors;

    public ApiError(int status, String error, String message, String path, List<FieldErrorDTO> fieldErrors) {
        this.timestamp = Instant.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
