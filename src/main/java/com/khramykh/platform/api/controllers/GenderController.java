package com.khramykh.platform.api.controllers;

import com.khramykh.platform.domain.commons.enums.UserGender;
import com.khramykh.platform.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/api/genders")
public class GenderController {
    @GetMapping
    public ResponseEntity getGenders() {
        return ResponseEntity.ok().body(UserGender.values());
    }
}
