package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.Chip;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.service.ChipsService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chips")
@Slf4j
public class ChipsController {

    private final ChipsService chipsService;
    private final SessionHelper sessionHelper;

    public ChipsController(ChipsService chipsService,
                           SessionHelper sessionHelper) {
        this.chipsService = chipsService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/house/{houseId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public List<Chip> getChipsByHouse(@PathVariable String houseId) {
        return chipsService.getChipsByHouse(houseId);
    }

    @PatchMapping("activate/{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip activateChip(@PathVariable String chipId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Activando chip: {} by user: {} - {}", chipId, loggedUser.getName(), loggedUser.getUserId());

        return chipsService.activateChip(chipId);
    }

    @PatchMapping("deactivate/{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip deactivateChip(@PathVariable String chipId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Desactivando chip: {} by user: {} - {}", chipId, loggedUser.getName(), loggedUser.getUserId());

        return chipsService.deactivateChip(chipId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip addChip(@RequestBody Chip chip, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Agregando chip: {} para casa {} by user: {} - {}", chip.getCode(), chip.getHouse(), loggedUser.getName(), loggedUser.getUserId());

        return chipsService.addChip(chip);
    }

    @DeleteMapping("{chipId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Chip removeChip(@PathVariable String chipId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Eliminando chip: {} by user: {} - {}", chipId, loggedUser.getName(), loggedUser.getUserId());

        return chipsService.deleteChip(chipId);
    }
}
