package com.khramykh.platform.application.usersApi.commands;

import com.khramykh.platform.domain.commons.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateCommand {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private MultipartFile file;
    private String gender;
    private String country;
    private String password;
}
