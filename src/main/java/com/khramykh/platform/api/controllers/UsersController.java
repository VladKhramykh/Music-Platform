package com.khramykh.platform.api.controllers;

import com.khramykh.platform.api.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UsersService usersService;

//    public ResponseEntity getUsersByPage(@RequestParam int pageNum, @RequestParam int pageSize) {
//    }
}
