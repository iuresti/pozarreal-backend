package org.uresti.pozarreal.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.ToggleChipStatusRequest;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.service.HousesService;

@RestController
@RequestMapping("/api/house")
@Slf4j
public class HousesController {

    private final HousesService housesService;

    public HousesController(HousesService housesService) {
        this.housesService = housesService;
    }

    @PutMapping("{houseId}/chips")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void toggleChipStatusRequest(@PathVariable String houseId, @RequestBody ToggleChipStatusRequest toggleChipStatusRequest) {
        housesService.toggleChipStatusRequest(toggleChipStatusRequest.getHouseId(), toggleChipStatusRequest.isEnable());
    }

    @GetMapping("/info/{houseId}")
    @PreAuthorize("hasAnyRole('ROLE_REPRESENTATIVE')")
    public HouseInfo getHouseInfo(@PathVariable String houseId) {
        return housesService.getHouseInfo(houseId);
    }

    @PatchMapping("/{houseId}/notes")
    @PreAuthorize("hasAnyRole('ROLE_REPRESENTATIVE')")
    public void saveNotes(@PathVariable String houseId, @RequestBody String notes) {
        housesService.saveNotes(houseId, notes);
    }
}
