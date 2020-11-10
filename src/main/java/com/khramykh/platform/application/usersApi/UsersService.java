package com.khramykh.platform.application.usersApi;

import com.khramykh.platform.application.exceptions.UserNotFoundException;
import com.khramykh.platform.application.repositories.UserRepository;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements IUserService {
    @Autowired
    public UserRepository userRepository;

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public Page<User> getUsersByPage(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize));
    }

    public Page<User> getOldestUsersByPage(int pageNum, int pageSize, Sort sort) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize), sort);
    }

    public void removeById(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public void update(User newUser) {
        User oldUser = userRepository.findById(newUser.getId()).orElseThrow(() -> new UserNotFoundException(newUser.getId()));
    }

    private boolean isExistsByEmail(String email) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        return user.isPresent();
    }
}
