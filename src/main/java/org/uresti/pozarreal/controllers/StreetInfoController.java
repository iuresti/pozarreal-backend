package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.service.StreetsService;

@RestController
@RequestMapping("/api/streetInfo")
@Slf4j
public class StreetInfoController {

    private final StreetsService streetsService;

    public StreetInfoController(StreetsService streetsService) {
        this.streetsService = streetsService;
    }

    @GetMapping("/{streetId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public ResponseEntity<StreetInfo> getStreetInfo(@PathVariable("streetId") String streetId) {
        return new ResponseEntity<>(streetsService.getStreetInfo(streetId), HttpStatus.OK);
    }
}
