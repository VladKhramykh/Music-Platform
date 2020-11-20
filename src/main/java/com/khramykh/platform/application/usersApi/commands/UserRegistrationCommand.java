package com.khramykh.platform.application.usersApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationCommand {
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String photoUri;
    private String gender;
    private String country;
    private String password;
}
