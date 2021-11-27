package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.Representative;
import org.uresti.pozarreal.repository.RepresentativeRepository;

public class RepresentativesServiceImplTests {

    @Test
    public void givenRepresentativeAndUserIdEquals_whenSave_thenRepresentativeIsSaved() {
        // Given:
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);

        RepresentativesServiceImpl representativesService = new RepresentativesServiceImpl(representativeRepository);

        Representative representative = Representative.builder()
                .house("house")
                .phone("123456")
                .street("street")
                .userId("userId")
                .build();

        org.uresti.pozarreal.dto.Representative representativeDto = org.uresti.pozarreal.dto.Representative.builder()
                .house("house")
                .phone("123456")
                .street("street")
                .userId("userId")
                .build();

        Mockito.when(representativeRepository.save(representative)).thenReturn(representative);

        // When:
        org.uresti.pozarreal.dto.Representative representativeSaved = representativesService.save(representativeDto, "userId");

        // Then:
        Assertions.assertThat(representativeSaved).isNotNull();
        Assertions.assertThat(representativeSaved.getHouse()).isEqualTo("house");
        Assertions.assertThat(representativeSaved.getPhone()).isEqualTo("123456");
        Assertions.assertThat(representativeSaved.getStreet()).isEqualTo("street");
        Assertions.assertThat(representativeSaved.getUserId()).isEqualTo("userId");
    }

    @Test
    public void givenRepresentativeAndUserIdNotEquals_whenSave_thenRepresentativeThrowBadRequestDataException() {
        // Given:
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);

        RepresentativesServiceImpl representativesService = new RepresentativesServiceImpl(representativeRepository);

        org.uresti.pozarreal.dto.Representative representativeDto = org.uresti.pozarreal.dto.Representative.builder()
                .house("house")
                .phone("123456")
                .street("street")
                .userId("userId")
                .build();

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> representativesService.save(representativeDto, "userId2"))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("UserId does not match with request body to update representative", "WRONG_USER_ID");

        Mockito.verifyNoMoreInteractions(representativeRepository);
        Mockito.verifyNoInteractions(representativeRepository);
    }

    @Test
    public void givenAndUserId_whenDelete_thenRepresentativeIsDeleted() {
        // Given:
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);

        RepresentativesServiceImpl representativesService = new RepresentativesServiceImpl(representativeRepository);

        // When:
        representativesService.delete("userId");

        // Then:
        Mockito.verify(representativeRepository).deleteById("userId");
    }

    @Test
    public void givenAndUserIdAndStreetId_whenSaveStreet_thenStreetIsSaved() {
        // Given:
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);

        RepresentativesServiceImpl representativesService = new RepresentativesServiceImpl(representativeRepository);

        ArgumentCaptor<Representative> argumentCaptor = ArgumentCaptor.forClass(Representative.class);

        Representative representative = Representative.builder()
                .house("house")
                .phone("123456")
                .street("street1")
                .userId("userId")
                .build();

        Mockito.when(representativeRepository.findById("userId")).thenReturn(java.util.Optional.ofNullable(representative));
        Mockito.when(representativeRepository.save(argumentCaptor.capture())).thenReturn(representative);

        // When:
        org.uresti.pozarreal.dto.Representative representativeSaved = representativesService.saveStreet("userId", "street");

        // Then:
        Representative parameter = argumentCaptor.getValue();

        Assertions.assertThat(parameter).isNotNull();
        Assertions.assertThat(parameter.getStreet()).isEqualTo("street");
        Assertions.assertThat(parameter.getStreet()).isNotEqualTo("street1");
        Assertions.assertThat(parameter.getStreet()).isNotEqualTo("street1");
        Assertions.assertThat(representativeSaved.getPhone()).isEqualTo("123456");
        Assertions.assertThat(representativeSaved.getUserId()).isEqualTo("userId");
        Assertions.assertThat(representativeSaved.getHouse()).isEqualTo("house");
    }
}