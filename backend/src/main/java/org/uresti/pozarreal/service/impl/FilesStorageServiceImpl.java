package org.uresti.pozarreal.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.uresti.pozarreal.model.Chip;
import org.uresti.pozarreal.service.ChipsService;
import org.uresti.pozarreal.service.DBUpdaterService;
import org.uresti.pozarreal.service.FilesStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    private final ChipsService chipsService;

    private final DBUpdaterService dbUpdaterService;

    public FilesStorageServiceImpl(ChipsService chipsService,
                                   DBUpdaterService dbUpdaterService) {
        this.chipsService = chipsService;
        this.dbUpdaterService = dbUpdaterService;
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Path save(MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            String fileName = UUID.randomUUID().toString();
            Path newFile = this.root.resolve(fileName);
            Files.copy(file.getInputStream(), newFile);
            List<Chip> chips = chipsService.getChipsWithRealStatus();

            dbUpdaterService.updateChips(chips, newFile.toAbsolutePath().toString());

            return newFile;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}