package org.uresti.pozarreal.service.impl;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.uresti.pozarreal.config.DropBoxConfig;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.model.Chip;
import org.uresti.pozarreal.model.PaymentEvidence;
import org.uresti.pozarreal.repository.PaymentEvidenceRepository;
import org.uresti.pozarreal.repository.PaymentRepository;
import org.uresti.pozarreal.service.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    private final ChipsService chipsService;

    private final DBUpdaterService dbUpdaterService;

    private final PaymentRepository paymentRepository;

    private final DropBoxConfig dropBoxConfig;

    private final PaymentEvidenceRepository paymentEvidenceRepository;

    public FilesStorageServiceImpl(ChipsService chipsService,
                                   DBUpdaterService dbUpdaterService,
                                   PaymentRepository paymentRepository,
                                   DropBoxConfig dropBoxConfig,
                                   PaymentEvidenceRepository paymentEvidenceRepository) {
        this.chipsService = chipsService;
        this.dbUpdaterService = dbUpdaterService;
        this.paymentRepository = paymentRepository;
        this.dropBoxConfig = dropBoxConfig;
        this.paymentEvidenceRepository = paymentEvidenceRepository;
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
    public String save(MultipartFile file, LoggedUser loggedUser, String paymentId) {

        paymentRepository.findById(paymentId).orElseThrow();

        DbxRequestConfig config = DbxRequestConfig.newBuilder(dropBoxConfig.getClientIdentifier()).build();
        DbxClientV2 client = new DbxClientV2(config, dropBoxConfig.getAccessToken());

        try {
            String originalName = file.getOriginalFilename();
            assert originalName != null;
            String extension = originalName.substring(originalName.indexOf('.'));
            String fileName = UUID.randomUUID().toString();
            client.files()
                    .uploadBuilder("/" + fileName + extension)
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(file.getInputStream());

            SharedLinkMetadata sharingRequests = client.sharing()
                    .createSharedLinkWithSettings("/" + fileName + extension);

            PaymentEvidence paymentEvidence = new PaymentEvidence();

            paymentEvidence.setId(UUID.randomUUID().toString());
            paymentEvidence.setPaymentId(paymentId);
            paymentEvidence.setUrl(sharingRequests.getUrl());

            paymentEvidenceRepository.save(paymentEvidence);

            return sharingRequests.getUrl();
        } catch (Exception e) {
            throw new PozarrealSystemException("Could not store the file. Error: " + e.getMessage(), "ERROR_SAVING_FILES");
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