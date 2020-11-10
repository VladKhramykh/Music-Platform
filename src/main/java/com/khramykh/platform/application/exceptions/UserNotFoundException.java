package com.khramykh.platform.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private int id;
    private String email;

    public UserNotFoundException(int id) {
        this.id = id;
    }

    public UserNotFoundException(String email) {
        this.email = email;
    }
}
