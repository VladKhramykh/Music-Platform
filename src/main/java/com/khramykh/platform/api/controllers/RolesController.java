package com.khramykh.platform.api.controllers;

import com.khramykh.platform.domain.commons.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/roles")
public class RolesController {
    @GetMapping
    public ResponseEntity getRoles() {
        return ResponseEntity.ok().body(Role.values());
    }
}
