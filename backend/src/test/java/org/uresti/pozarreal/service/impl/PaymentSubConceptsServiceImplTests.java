package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.PaymentSubConcept;
import org.uresti.pozarreal.repository.PaymentSubConceptsRepository;

import java.util.LinkedList;
import java.util.List;

public class PaymentSubConceptsServiceImplTests {

    @Test
    public void givenPaymentConceptId_whenFindAllByConcept_thenPaymentSubConceptListEmptyIsReturned() {
        // Given:
        PaymentSubConceptsRepository paymentSubConceptsRepository = Mockito.mock(PaymentSubConceptsRepository.class);

        PaymentSubConceptsServiceImpl paymentSubConceptsService = new PaymentSubConceptsServiceImpl(paymentSubConceptsRepository);

        List<PaymentSubConcept> paymentSubConcepts = new LinkedList<>();

        Mockito.when(paymentSubConceptsRepository.findAllByConceptId("id")).thenReturn(paymentSubConcepts);

        // When:
        List<org.uresti.pozarreal.dto.PaymentSubConcept> paymentSubConceptList = paymentSubConceptsService
                .findAllByConcept("id");

        // Then:
        Assertions.assertThat(paymentSubConceptList.isEmpty()).isTrue();
    }

    @Test
    public void givenPaymentConceptId_whenFindAllByConcept_thenPaymentSubConceptListWithOneElementIsReturned() {
        // Given:
        PaymentSubConceptsRepository paymentSubConceptsRepository = Mockito.mock(PaymentSubConceptsRepository.class);

        PaymentSubConceptsServiceImpl paymentSubConceptsService = new PaymentSubConceptsServiceImpl(paymentSubConceptsRepository);

        List<PaymentSubConcept> paymentSubConcepts = new LinkedList<>();

        PaymentSubConcept paymentSubConcept = PaymentSubConcept.builder()
                .conceptId("concept")
                .id("id")
                .label("label")
                .build();

        paymentSubConcepts.add(paymentSubConcept);

        Mockito.when(paymentSubConceptsRepository.findAllByConceptId("id")).thenReturn(paymentSubConcepts);

        // When:
        List<org.uresti.pozarreal.dto.PaymentSubConcept> paymentSubConceptList = paymentSubConceptsService
                .findAllByConcept("id");

        // Then:
        Assertions.assertThat(paymentSubConceptList.size()).isEqualTo(1);
    }

    @Test
    public void givenPaymentConcept_whenSave_thenPaymentConceptIsSaved() {
        // Given:
        PaymentSubConceptsRepository paymentSubConceptsRepository = Mockito.mock(PaymentSubConceptsRepository.class);

        PaymentSubConceptsServiceImpl paymentSubConceptsService = new PaymentSubConceptsServiceImpl(paymentSubConceptsRepository);

        ArgumentCaptor<PaymentSubConcept> argumentCaptor = ArgumentCaptor.forClass(PaymentSubConcept.class);

        PaymentSubConcept paymentSubConcept = PaymentSubConcept.builder()
                .conceptId("concept")
                .id("id")
                .label("label")
                .build();

        org.uresti.pozarreal.dto.PaymentSubConcept paymentSubConceptDto = org.uresti.pozarreal.dto.PaymentSubConcept.builder()
                .conceptId("concept")
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentSubConceptsRepository.save(argumentCaptor.capture())).thenReturn(paymentSubConcept);

        // When:
        org.uresti.pozarreal.dto.PaymentSubConcept paymentSubConceptSaved = paymentSubConceptsService.save(paymentSubConceptDto);

        // Then:
        PaymentSubConcept parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentSubConceptSaved).isNotNull();
        Assertions.assertThat(parameter).isNotNull();
        Assertions.assertThat(parameter.getId()).isNotEqualTo(paymentSubConceptSaved.getId());
        Assertions.assertThat(paymentSubConceptSaved.getConceptId()).isEqualTo("concept");
        Assertions.assertThat(paymentSubConceptSaved.getLabel()).isEqualTo("label");
    }

    @Test
    public void givenPaymentConcept_whenUpdate_thenPaymentConceptIsUpdated() {
        // Given:
        PaymentSubConceptsRepository paymentSubConceptsRepository = Mockito.mock(PaymentSubConceptsRepository.class);

        PaymentSubConceptsServiceImpl paymentSubConceptsService = new PaymentSubConceptsServiceImpl(paymentSubConceptsRepository);

        PaymentSubConcept paymentSubConcept = PaymentSubConcept.builder()
                .conceptId("concept")
                .id("id")
                .label("label")
                .build();

        org.uresti.pozarreal.dto.PaymentSubConcept paymentSubConceptDto = org.uresti.pozarreal.dto.PaymentSubConcept.builder()
                .conceptId("concept")
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentSubConceptsRepository.save(paymentSubConcept)).thenReturn(paymentSubConcept);

        // When:
        org.uresti.pozarreal.dto.PaymentSubConcept paymentSubConceptSaved = paymentSubConceptsService.update(paymentSubConceptDto);

        // Then:
        Assertions.assertThat(paymentSubConceptSaved).isNotNull();
        Assertions.assertThat(paymentSubConceptSaved.getId()).isEqualTo("id");
        Assertions.assertThat(paymentSubConceptSaved.getConceptId()).isEqualTo("concept");
        Assertions.assertThat(paymentSubConceptSaved.getLabel()).isEqualTo("label");
    }

    @Test
    public void givenPaymentConceptWithIdNull_whenUpdate_thenThrowBadRequestDataException() {
        // Given:
        PaymentSubConceptsRepository paymentSubConceptsRepository = Mockito.mock(PaymentSubConceptsRepository.class);

        PaymentSubConceptsServiceImpl paymentSubConceptsService = new PaymentSubConceptsServiceImpl(paymentSubConceptsRepository);

        org.uresti.pozarreal.dto.PaymentSubConcept paymentSubConceptDto = org.uresti.pozarreal.dto.PaymentSubConcept.builder()
                .conceptId("concept")
                .label("label")
                .build();

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentSubConceptsService.update(paymentSubConceptDto))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Missing payment sub concept id", "REQUIRED_PAYMENT_SUB_CONCEPT_ID");

        Mockito.verifyNoInteractions(paymentSubConceptsRepository);
    }
}
