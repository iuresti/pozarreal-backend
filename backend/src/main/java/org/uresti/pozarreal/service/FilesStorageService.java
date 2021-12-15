package org.uresti.pozarreal.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.uresti.pozarreal.dto.LoggedUser;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {

    void init();

    Path save(MultipartFile file);

    String save(MultipartFile file, LoggedUser loggedUser, String paymentId);

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();
}
