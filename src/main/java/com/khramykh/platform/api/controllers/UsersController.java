package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.commons.sort.UserSort;
import com.khramykh.platform.application.usersApi.UsersService;
import com.khramykh.platform.application.usersApi.commands.UserRegistrationCommand;
import com.khramykh.platform.application.usersApi.commands.UserUpdateCommand;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getUsersByPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam UserSort userSort,
            @RequestParam(required = false) String filter
    ) {
        Page<User> page;
        if (filter != null) {
            page = usersService.getUserByFilterContaining(filter, pageNum, pageSize, userSort);
        } else {
            page = usersService.getUsersByPage(pageNum, pageSize, userSort);
        }
        return ResponseEntity.ok().body(page);
    }

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

    @PostMapping
    public ResponseEntity<User> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserRegistrationCommand command
    ) throws ParseException, IOException {
        User created = usersService.registration(command, userDetails.getUsername());
        return ResponseEntity.ok().body(created);
    }

    @PutMapping
    public ResponseEntity<User> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateCommand command
    ) throws ParseException, IOException {
        User updated = usersService.update(command, userDetails.getUsername());
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity removeById(@PathVariable int id) {
        usersService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
