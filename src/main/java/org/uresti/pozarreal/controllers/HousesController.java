package org.uresti.pozarreal.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.ToggleChipStatusRequest;
import org.uresti.pozarreal.service.HousesService;

@RestController
@RequestMapping("/api/house")
public class HousesController {

    private final HousesService housesService;

    public HousesController(HousesService housesService) {
        this.housesService = housesService;
    }

    @PutMapping("chips")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public void toggleChipStatusRequest( @RequestBody ToggleChipStatusRequest toggleChipStatusRequest) {
        housesService.toggleChipStatusRequest(toggleChipStatusRequest.getHouseId(), toggleChipStatusRequest.isEnable());
    }
}
