package com.khramykh.platform.domain.entities;

import com.khramykh.platform.domain.commons.enums.Country;
import com.khramykh.platform.domain.commons.enums.Role;
import com.khramykh.platform.domain.commons.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @NotNull(message = "{user.firstnamenotnull}")
    @NotEmpty(message = "{user.firstnamenotempty}")
    private String firstName;

    @NotNull(message = "{user.lastnamenotnull}")
    @NotEmpty(message = "{user.lastnamenotempty}")
    private String lastName;

    @Email(message = "{user.incorrectemail}")
    @NotNull(message = "{user.emailnotnull}")
    @NotEmpty(message = "{user.emailnotempty}")
    private String email;

    @NotNull(message = "{user.birthdaynotnull}")
    @PastOrPresent
    private Date birthday;

    private String photoUri;

    @NotNull(message = "{user.gendernotnull}")
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @NotNull(message = "{user.countrynotnull}")
    @Enumerated(EnumType.STRING)
    private Country country;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    private String hashPassword;
}
