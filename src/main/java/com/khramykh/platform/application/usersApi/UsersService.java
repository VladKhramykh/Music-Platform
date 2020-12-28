package com.khramykh.platform.application.usersApi;

import com.khramykh.platform.application.commons.sort.UserSort;
import com.khramykh.platform.application.commons.utils.FileHelper;
import com.khramykh.platform.application.commons.utils.FileOperations;
import com.khramykh.platform.application.exceptions.EmailAlreadyInUseException;
import com.khramykh.platform.application.exceptions.UserNotFoundException;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.application.usersApi.commands.UserRegistrationCommand;
import com.khramykh.platform.application.usersApi.commands.UserUpdateCommand;
import com.khramykh.platform.domain.commons.enums.Country;
import com.khramykh.platform.domain.commons.enums.Role;
import com.khramykh.platform.domain.commons.enums.UserGender;
import com.khramykh.platform.domain.entities.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileHelper fileHelper;

    @Value("${upload.path}")
    private String uploadPath;

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

    public User update(UserUpdateCommand command, String lastModifiedBy) throws ParseException {
        User oldUser = usersRepository.findById(command.getId()).orElseThrow(() -> new UserNotFoundException(command.getId()));
        User updated;
        oldUser.setLastModifiedBy(lastModifiedBy);
        if (command.getPassword().trim().length() > 0) {
            updated = usersRepository.save(convertUserUpdateCommandToUser(oldUser, command, true));
        } else {
            updated = usersRepository.save(convertUserUpdateCommandToUser(oldUser, command, false));
        }
        return updated;
    }

    private boolean isExistsByEmail(String email) {
        Optional<User> user = usersRepository.findByEmailIgnoreCase(email);
        return user.isPresent();
    }

    public User registration(UserRegistrationCommand command, String createdBy) throws ParseException, IOException {
        if (isExistsByEmail(command.getEmail())) {
            throw new EmailAlreadyInUseException("There is an account with that email adress: " + command.getEmail());
        }
        User user = new User();

        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setEmail(command.getEmail());
        user.setPhotoUri(Strings.EMPTY);
        // TODO need to fix date persing (maybe need to change type of birthday)
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(command.getBirthday().substring(0, 10)));
        user.setGender(UserGender.valueOf(command.getGender()));
        user.setCountry(Country.valueOf(command.getCountry()));
        user.setHashPassword(passwordEncoder.encode(command.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setCreatedBy(createdBy);
        return usersRepository.save(user);
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
            case ROLES_ASC:
                return Sort.by("roles").ascending();
            case ROLES_DESC:
                return Sort.by("gender").descending();
            case GENDER_ASC:
                return Sort.by("gender").ascending();
            case GENDER_DESC:
                return Sort.by("roles").descending();
            case LASTNAME_ASC:
                return Sort.by("lastName").ascending();
            case LASTNAME_DESC:
                return Sort.by("lastName").descending();
            case FIRSTNAME_ASC:
                return Sort.by("firstName").ascending();
            case FIRSTNAME_DESC:
                return Sort.by("firstName").descending();
            case BIRTHDAY_ASC:
                return Sort.by("birthday").ascending();
            case BIRTHDAY_DESC:
                return Sort.by("birthday").descending();
            default:
                return Sort.by("id").ascending();
        }
    }

    private User convertUserUpdateCommandToUser(User oldUser, UserUpdateCommand command, boolean isPasswordChanged) throws ParseException {
        oldUser.setFirstName(command.getFirstName());
        oldUser.setLastName(command.getLastName());
        if (!oldUser.getEmail().equals(command.getEmail())) {
            if (usersRepository.findByEmailIgnoreCase(command.getEmail()).isEmpty()) {
                oldUser.setEmail(command.getEmail());
            } else {
                throw new EmailAlreadyInUseException();
            }
        }
        oldUser.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(command.getBirthday()));
        oldUser.setCountry(Country.valueOf(command.getCountry()));
        if (isPasswordChanged) {
            oldUser.setHashPassword(passwordEncoder.encode(command.getPassword()));
        }
        if (command.getRoles() != null && command.getRoles().size() != 0) {
            oldUser.getRoles().clear();
            oldUser.setRoles(command.getRoles());
        }

        oldUser.setGender(UserGender.valueOf(command.getGender()));

        return oldUser;
    }

    public String updatePhoto(int id, MultipartFile file) throws IOException {
        User user = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        fileHelper.deleteFile(user.getPhotoUri(), FileOperations.USER_PHOTO);
        user.setPhotoUri(fileHelper.getNewUri(file, FileOperations.USER_PHOTO));
        usersRepository.save(user);
        return user.getPhotoUri();
    }

    public Page<User> getUserByFilterContaining(String filter, int pageNum, int pageSize, UserSort userSort) {
        return usersRepository
                .findByFilterContaining(
                        filter,
                        PageRequest.of(pageNum, pageSize, getSortType(userSort))
                );
    }
}
