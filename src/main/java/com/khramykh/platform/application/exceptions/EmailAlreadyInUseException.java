package com.khramykh.platform.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAlreadyInUseException extends RuntimeException {
    private String email;
}
