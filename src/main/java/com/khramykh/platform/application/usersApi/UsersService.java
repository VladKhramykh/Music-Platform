package com.khramykh.platform.application.usersApi;

import com.khramykh.platform.application.commons.sort.UserSort;
import com.khramykh.platform.application.exceptions.EmailAlreadyInUseException;
import com.khramykh.platform.application.exceptions.UserNotFoundException;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.application.usersApi.commands.UserRegistrationCommand;
import com.khramykh.platform.application.usersApi.commands.UserUpdateCommand;
import com.khramykh.platform.domain.commons.enums.Country;
import com.khramykh.platform.domain.commons.enums.Role;
import com.khramykh.platform.domain.commons.enums.UserGender;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    public User getUserById(int id) {
        return usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByEmail(String email) {
        return usersRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public Page<User> getUsersByPage(int pageNum, int pageSize, UserSort userSort) {
        return usersRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(userSort)));
    }

    public void removeById(int id) {
        if (!usersRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        usersRepository.deleteById(id);
    }

    public User update(UserUpdateCommand command) throws ParseException {
        User oldUser = usersRepository.findById(command.getId()).orElseThrow(() -> new UserNotFoundException(command.getId()));
        User updated = usersRepository.save(convertUserUpdateCommandToUser(oldUser, command));
        return updated;
    }

    private boolean isExistsByEmail(String email) {
        Optional<User> user = usersRepository.findByEmailIgnoreCase(email);
        return user.isPresent();
    }

    public User registration(UserRegistrationCommand command) throws ParseException {
        if (isExistsByEmail(command.getEmail())) {
            throw new EmailAlreadyInUseException("There is an account with that email adress: " + command.getEmail());
        }
        User user = new User();

        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setEmail(command.getEmail());
        // TODO need to fix date persing (maybe need to change type of birthday)
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(command.getBirthday().substring(0,10)));
        user.setGender(UserGender.valueOf(command.getGender()));
        user.setCountry(Country.valueOf(command.getCountry()));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setHashPassword(passwordEncoder.encode(command.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));

        new Thread(() -> sendActivationCode(user)).start();

        return usersRepository.save(user);
    }

    private void sendActivationCode(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s %s \n" +
                            "Welcome to our music platform. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getActivationCode()
            );
            // mailSender.send(user.getEmail(), "Activation Code [no spam]", message);
        }
    }

    public boolean activateUser(String code) {
        Optional<User> user = usersRepository.findUserByActivationCode(code);
        if (user.isEmpty()) {
            return false;
        }
        user.get().setActivationCode(null);
        usersRepository.save(user.get());

        return true;
    }

    private Sort getSortType(UserSort userSort) {
        switch (userSort) {
            case ID_DESC:
                return Sort.by("id").descending();
            case EMAIL_ASC:
                return Sort.by("email").ascending();
            case EMAIL_DESC:
                return Sort.by("email").descending();
            case COUNTRY_ASC:
                return Sort.by("country").ascending();
            case COUNTRY_DESC:
                return Sort.by("country").descending();
            case LAST_NAME_ASC:
                return Sort.by("lastName").ascending();
            case LAST_NAME_DESC:
                return Sort.by("lastName").descending();
            case FIRST_NAME_ASC:
                return Sort.by("firstName").ascending();
            case FIRST_NAME_DESC:
                return Sort.by("firstName").descending();
            case DATE_OF_REGISTRATION_ASC:
                return Sort.by("dateOfRegistration").ascending();
            case DATE_OF_REGISTRATION_DESC:
                return Sort.by("dateOfRegistration").descending();
            default:
                return Sort.by("id").ascending();
        }
    }

    private User convertUserUpdateCommandToUser(User oldUser, UserUpdateCommand command) throws ParseException {
        oldUser.setFirstName(command.getFirstName());
        oldUser.setLastName(command.getLastName());
        if (!oldUser.getEmail().equals(command.getEmail())) {
            oldUser.setActivationCode(UUID.randomUUID().toString());
        }
        oldUser.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse(command.getBirthday()));
        oldUser.setCountry(Country.valueOf(command.getCountry()));
        oldUser.setPhotoUri(command.getPhotoUri());
        oldUser.setHashPassword(command.getPassword());
        oldUser.setGender(UserGender.valueOf(command.getGender()));

        return oldUser;
    }
}
