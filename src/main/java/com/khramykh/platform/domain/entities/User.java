package com.khramykh.platform.domain.entities;

import com.khramykh.platform.domain.commons.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "{user.firstnamenotnull}")
    @NotEmpty(message = "{user.firstnamenotempty}")
    private String firstName;
    @NotNull(message = "{user.lastnamenotnull}")
    @NotEmpty(message = "{user.lastnamenotempty}")
    private String lastName;
    private String activationCode;
    @Email(message = "{user.incorrectemail}")
    @NotNull(message = "{user.emailnotnull}")
    @NotEmpty(message = "{user.emailnotempty}")
    private String email;
    @NotNull(message = "{user.birthdaynotnull}")
    private Date birthday;
    @PastOrPresent
    private Timestamp dateOfRegistration;
    private String photoUri;
    @NotNull(message = "{user.gendernotnull}")
    private UserGender gender;
    @NotNull(message = "{user.countrynotnull}")
    @NotEmpty(message = "{user.countrynotempty}")
    private String country;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    private String hashPassword;
}
