package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.commons.sort.UserSort;
import com.khramykh.platform.application.usersApi.UsersService;
import com.khramykh.platform.application.usersApi.commands.UserRegistrationCommand;
import com.khramykh.platform.application.usersApi.commands.UserUpdateCommand;
import com.khramykh.platform.domain.commons.enums.Role;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/users")
public class UsersController {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    UsersService usersService;

    @GetMapping("{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        User foundUser = usersService.getUserById(id);
        return ResponseEntity.ok().body(foundUser);
    }

    @PostMapping("/photo")
    public ResponseEntity setPhoto(@RequestParam int id, @RequestParam(name = "file") MultipartFile file) throws IOException {
        String photoUrl = usersService.updatePhoto(id, file);
        return ResponseEntity.ok().body(photoUrl);
    }

    @GetMapping
    public ResponseEntity getUsersByPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam UserSort userSort) {
        Page<User> page = usersService.getUsersByPage(pageNum, pageSize, userSort);
        return ResponseEntity.ok().body(page);
    }
//
//    @GetMapping
//    public ResponseEntity getUserByEmail(@RequestParam String email) {
//        User page = usersService.getUserByEmail(email);
//        return ResponseEntity.ok().body(page);
//    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRegistrationCommand command) throws ParseException, IOException {
        User created = usersService.registration(command);
        return ResponseEntity.ok().body(created);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody UserUpdateCommand command) throws ParseException, IOException {
        User updated = usersService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity removeById(@AuthenticationPrincipal User currentUser, @PathVariable int id) {
        if (currentUser.getId() == id || currentUser.getRoles().contains(Role.ADMIN)) {
            usersService.removeById(id);
            return (ResponseEntity) ResponseEntity.ok();
        } else {
            return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
        }
    }
}
