package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.Representative;
import org.uresti.pozarreal.service.RepresentativesService;

@RestController
@RequestMapping("/api/representatives")
@Slf4j
public class RepresentativeController {

    private final RepresentativesService representativeService;

    public RepresentativeController(RepresentativesService representativeService) {
        this.representativeService = representativeService;
    }

    @PatchMapping("/{userId}/street/{streetId}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public Representative saveStreet(@PathVariable String userId, @PathVariable String streetId) {
        return representativeService.saveStreet(userId, streetId);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public void delete(@PathVariable String userId) {
        representativeService.delete(userId);
    }
}
