package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.service.StreetsService;

import java.util.List;

@RestController
@RequestMapping("/api/streets")
@Slf4j
public class StreetsController {

    private final StreetsService streetsService;
    private final HousesService housesService;

    public StreetsController(StreetsService streetsService,
                             HousesService housesService) {
        this.streetsService = streetsService;
        this.housesService = housesService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public ResponseEntity<List<Street>> getStreets() {
        List<Street> streets = streetsService.getStreets();


        return new ResponseEntity<>(streets, HttpStatus.OK);
    }

    @GetMapping("{streetId}/houses")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public ResponseEntity<List<House>> getHousesByStreet(@PathVariable String streetId) {
        List<House> houses = housesService.getHousesByStreet(streetId);

        return new ResponseEntity<>(houses, HttpStatus.OK);
    }
}
