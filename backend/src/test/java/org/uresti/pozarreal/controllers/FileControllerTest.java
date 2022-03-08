package org.uresti.pozarreal.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.service.FilesStorageService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

@WebMvcTest(FileController.class)
@ActiveProfiles("tests")
public class FileControllerTest {
    private final String BASE_URL = "/api/chips-database";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilesStorageService filesStorageService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAFile_whenSave_thenFileIsSaved() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        String url = UUID.randomUUID() + ".txt";

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(filesStorageService.save(file, loggedUser, paymentId)).thenReturn(url);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(BASE_URL + "/upload/" + paymentId)
                                .file(file))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(filesStorageService).save(file, loggedUser, paymentId);
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAFileWithError_whenSave_thenExceptionIsThrown() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(filesStorageService.save(file, loggedUser, paymentId)).thenThrow(new BadRequestDataException("bad request", "NONE"));
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(BASE_URL + "/upload/" + paymentId)
                                .file(file))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());

        Mockito.verify(filesStorageService).save(file, loggedUser, paymentId);
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }


    @Test
    @WithMockUser(username = "test", password = "test", roles = "CHIPS_UPDATER")
    public void givenAFile_whenUploadFile_thenFileIsSaved() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Path savedFile = Path.of(paymentId + ".txt");

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(filesStorageService.save(file)).thenReturn(savedFile);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(BASE_URL + "/upload")
                                .file(file))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(filesStorageService).save(file);
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "CHIPS_UPDATER")
    public void givenAFileWithError_whenUploadFile_thenExceptionIsThrown() throws Exception {
        // Given:
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(filesStorageService.save(file)).thenThrow(new BadRequestDataException("bad request", "NONE"));
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(BASE_URL + "/upload")
                                .file(file))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());

        Mockito.verify(filesStorageService).save(file);
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "CHIPS_UPDATER")
    public void given_whenLoadAll_thenStreamOfPathsIsReturned() throws Exception {
        // Given:
        Stream<Path> pathStream = Files.walk(Path.of(""), 1);

        Mockito.when(filesStorageService.loadAll()).thenReturn(pathStream);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/files"))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(filesStorageService).loadAll();
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "CHIPS_UPDATER")
    public void givenAFilename_whenLoad_thenFileIsFindAndReturned() throws Exception {
        // Given:
        String filename = UUID.randomUUID().toString();

        Path file = Files.createTempFile(filename, ".png");

        Resource resource = new UrlResource(file.toUri());

        Mockito.when(filesStorageService.load(filename + ".png")).thenReturn(resource);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/files/" + filename + ".png"))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.containsHeader(HttpHeaders.CONTENT_DISPOSITION)).isTrue();

        Mockito.verify(filesStorageService).load(filename + ".png");
        Mockito.verifyNoMoreInteractions(filesStorageService);
    }
}
