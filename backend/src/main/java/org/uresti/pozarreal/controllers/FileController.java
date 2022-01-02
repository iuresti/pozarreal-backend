package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.dto.FileInfo;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.service.FilesStorageService;

import java.nio.file.Path;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/chips-database")
@Slf4j
public class FileController {

    private final FilesStorageService storageService;

    private final SessionHelper sessionHelper;

    public FileController(FilesStorageService storageService,
                          SessionHelper sessionHelper) {
        this.storageService = storageService;
        this.sessionHelper = sessionHelper;
    }

    @PostMapping("/upload/{paymentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file,
                                                          Principal principal,
                                                          @PathVariable String paymentId) {
        Map<String, Object> response = new HashMap<>();
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        try {
            log.info("Receiving payment voucher {} by user {}, admin = {}, representative = {}", paymentId,
                    loggedUser.getName(), sessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN),
                    sessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE));

            String url = storageService.save(file, loggedUser, paymentId);

            response.put("message", "Archivo subido exitosamente");
            response.put("url", url);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            String message = "Error al subir el archivo: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Collections.singletonMap("message", message));
        }
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ROLE_CHIPS_UPDATER')")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            Path savedFile = storageService.save(file);

            String filename = savedFile.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", savedFile.getFileName().toString()).build().toString();

            response.put("message", "Base de datos actualizada");
            response.put("fileInfo", new FileInfo(filename, url));

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            String message = "Error al subir el archivo: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Collections.singletonMap("message", message));
        }
    }

    @GetMapping("/files")
    @PreAuthorize("hasAnyRole('ROLE_CHIPS_UPDATER')")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_CHIPS_UPDATER')")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Property2.mdb").body(file);
    }
}
