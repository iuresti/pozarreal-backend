package org.uresti.pozarreal.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.Chip;
import org.uresti.pozarreal.service.ChipsService;

import java.util.List;

@RestController
@RequestMapping("/api/chips")
public class ChipsController {

    private final ChipsService chipsService;

    public ChipsController(ChipsService chipsService) {
        this.chipsService = chipsService;
    }

    @GetMapping("/house/{houseId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESIDENT')")
    public List<Chip> getChipsByHouse(@PathVariable String houseId) {
        return chipsService.getChipsByHouse(houseId);
    }


    @PatchMapping("activate/{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip activateChip(@PathVariable String chipId) {
        return chipsService.activateChip(chipId);
    }

    @PatchMapping("deactivate/{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip deactivateChip(@PathVariable String chipId) {
        return chipsService.deactivateChip(chipId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip addChip(@RequestBody Chip chip) {
        return chipsService.addChip(chip);
    }

    @DeleteMapping("{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip addChip(@PathVariable String chipId) {
        return chipsService.deleteChip(chipId);
    }


}
