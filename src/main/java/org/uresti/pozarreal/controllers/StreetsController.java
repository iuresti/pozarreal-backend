package org.uresti.pozarreal.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.service.StreetsService;

@RestController
@RequestMapping("/api/streets")
public class StreetsController {

    private final StreetsService streetsService;

    public StreetsController(StreetsService streetsService) {
        this.streetsService = streetsService;
    }

    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public ResponseEntity<List<Street>> getStreets(OAuth2AuthenticationToken authentication) {
        List<Street> streets = streetsService.getStreets();


        return new ResponseEntity<>(streets, HttpStatus.OK);
    }
}
