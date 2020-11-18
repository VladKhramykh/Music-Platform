package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.commons.sort.UserSort;
import com.khramykh.platform.application.usersApi.UsersService;
import com.khramykh.platform.application.usersApi.commands.UserRegistrationCommand;
import com.khramykh.platform.application.usersApi.commands.UserUpdateCommand;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @GetMapping("{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        User foundUser = usersService.getUserById(id);
        return ResponseEntity.ok().body(foundUser);
    }

    @GetMapping
    public ResponseEntity getUserByPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam UserSort userSort) {
        Page<User> page = usersService.getUsersByPage(pageNum, pageSize, userSort);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRegistrationCommand command) {
        User created = usersService.registration(command);
        return ResponseEntity.ok().body(created);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody UserUpdateCommand command) {
        User updated = usersService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity removeById(@AuthenticationPrincipal User currentUser, @PathVariable int id) {
        if (currentUser.getId() == id) {
            usersService.removeById(id);
            return (ResponseEntity) ResponseEntity.ok();
        } else {
            return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
        }
    }
}
