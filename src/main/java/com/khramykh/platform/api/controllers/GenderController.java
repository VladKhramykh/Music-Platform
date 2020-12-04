package com.khramykh.platform.api.controllers;

import com.khramykh.platform.domain.commons.enums.UserGender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/genders")
public class GenderController {
    @GetMapping
    public ResponseEntity getGenders() {
        return ResponseEntity.ok().body(UserGender.values());
    }
}
