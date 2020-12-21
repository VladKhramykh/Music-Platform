package com.khramykh.platform.api.controllers;

import com.khramykh.platform.domain.commons.enums.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/api/countries")
public class CountriesController {
    @GetMapping
    public ResponseEntity getCountries() {
        Country[] countryEnum = Country.values();
        Arrays.sort(countryEnum);
        return ResponseEntity.ok().body(Arrays.stream(countryEnum));
    }
}
