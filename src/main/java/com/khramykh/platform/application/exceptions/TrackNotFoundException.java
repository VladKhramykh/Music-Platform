package com.khramykh.platform.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackNotFoundException extends RuntimeException {
    private int id;
    private String name;
}
