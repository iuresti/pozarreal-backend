package org.uresti.pozarreal.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.HouseByUser;
import org.uresti.pozarreal.dto.ToggleChipStatusRequest;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.service.HousesService;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('ROLE_REPRESENTATIVE', 'ROLE_ADMIN')")
    public HouseInfo getHouseInfo(@PathVariable String houseId) {
        return housesService.getHouseInfo(houseId);
    }

    @PatchMapping("/{houseId}/notes")
    @PreAuthorize("hasAnyRole('ROLE_REPRESENTATIVE', 'ROLE_ADMIN')")
    public void saveNotes(@PathVariable String houseId, @RequestBody String notes) {
        housesService.saveNotes(houseId, notes);
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public List<HouseByUser> getHousesByUser(@PathVariable String userId) {
        return housesService.getHousesByUser(userId);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public void deleteHouseByUser(@PathVariable String id) {
        housesService.deleteHouseByUser(id);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public HouseByUser saveHouseByUser(@RequestBody HouseByUser houseByUser) {
        return housesService.saveHouseByUser(houseByUser);
    }
}
