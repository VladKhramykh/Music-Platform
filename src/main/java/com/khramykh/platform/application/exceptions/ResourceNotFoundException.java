package com.khramykh.platform.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private int id;
    private String msg;

    public ResourceNotFoundException(int id) {
        this.id = id;
    }

    public ResourceNotFoundException(String msg) {
        this.msg = msg;
    }
}
