package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.uresti.pozarreal.dto.PaymentConcept;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.repository.PaymentConceptsRepository;
import org.uresti.pozarreal.service.mappers.PaymentConceptMapper;

import java.util.LinkedList;
import java.util.List;

public class PaymentConceptsServiceImplTests {

    @Test
    public void givenAnEmptyPaymentConceptList_whenFindAll_thenEmptyListIsReturned() {
        //Given:
        PaymentConceptsRepository paymentConceptsRepository = Mockito.mock(PaymentConceptsRepository.class);
        PaymentConceptsServiceImpl paymentConceptsService = new PaymentConceptsServiceImpl(paymentConceptsRepository);

        List<org.uresti.pozarreal.model.PaymentConcept> paymentConcepts = new LinkedList<>();

        Mockito.when(paymentConceptsRepository.findAll()).thenReturn(paymentConcepts);

        //When:
        List<org.uresti.pozarreal.dto.PaymentConcept> paymentConceptList = paymentConceptsService.findAll();

        //Then:
        Assertions.assertThat(paymentConceptList.isEmpty()).isTrue();
    }

    @Test
    public void givenAnPaymentConceptListWithTwoElements_whenFindAll_thenListIsReturnedWithTwoElements() {
        //Given:
        PaymentConceptsRepository paymentConceptsRepository = Mockito.mock(PaymentConceptsRepository.class);
        PaymentConceptsServiceImpl paymentConceptsService = new PaymentConceptsServiceImpl(paymentConceptsRepository);

        List<org.uresti.pozarreal.model.PaymentConcept> paymentConcepts = new LinkedList<>();

        org.uresti.pozarreal.model.PaymentConcept paymentConcept1 = org.uresti.pozarreal.model.PaymentConcept.builder()
                .id("id1")
                .label("label1")
                .build();

        org.uresti.pozarreal.model.PaymentConcept paymentConcept2 = org.uresti.pozarreal.model.PaymentConcept.builder()
                .id("id2")
                .label("label2")
                .build();

        paymentConcepts.add(paymentConcept1);
        paymentConcepts.add(paymentConcept2);

        Mockito.when(paymentConceptsRepository.findAll()).thenReturn(paymentConcepts);

        //When:
        List<org.uresti.pozarreal.dto.PaymentConcept> paymentConceptList = paymentConceptsService.findAll();

        //Then:
        Assertions.assertThat(paymentConceptList.size()).isEqualTo(2);
        Assertions.assertThat(paymentConceptList.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(paymentConceptList.get(0).getLabel()).isEqualTo("label1");
        Assertions.assertThat(paymentConceptList.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(paymentConceptList.get(1).getLabel()).isEqualTo("label2");
    }

    @Test
    public void givenAnPaymentConcept_whenSave_thenPaymentConceptIsSaved() {
        //Given:
        PaymentConceptsRepository paymentConceptsRepository = Mockito.mock(PaymentConceptsRepository.class);

        PaymentConceptsServiceImpl paymentConceptsService = new PaymentConceptsServiceImpl(paymentConceptsRepository);

        ArgumentCaptor<org.uresti.pozarreal.model.PaymentConcept> argumentCaptor =
                ArgumentCaptor.forClass(org.uresti.pozarreal.model.PaymentConcept.class);

        PaymentConcept paymentConceptDto = PaymentConcept.builder()
                .id("id1")
                .label("label1")
                .build();

        org.uresti.pozarreal.model.PaymentConcept paymentConcept = org.uresti.pozarreal.model.PaymentConcept.builder()
                .id("id2")
                .label("label2")
                .build();

        Mockito.when(paymentConceptsRepository.save(argumentCaptor.capture())).thenReturn(paymentConcept);

        //When:

        PaymentConcept paymentConceptSaved = paymentConceptsService.save(paymentConceptDto);

        //Then:
        org.uresti.pozarreal.model.PaymentConcept parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentConceptSaved).isNotNull();
        Assertions.assertThat(parameter.getId()).isNotNull();
        Assertions.assertThat(parameter.getId()).isNotEqualTo("id1");
        Assertions.assertThat(parameter.getLabel()).isEqualTo("label1");
        Assertions.assertThat(paymentConceptSaved.getId()).isEqualTo("id2");
        Assertions.assertThat(paymentConceptSaved.getLabel()).isEqualTo("label2");

        Mockito.verify(paymentConceptsRepository).save(parameter);
        Mockito.verifyNoMoreInteractions(paymentConceptsRepository);
    }

    @Test
    public void givenAnPaymentConcept_whenUpdate_thenPaymentConceptIsUpdated() {
        //Given:
        PaymentConceptsRepository paymentConceptsRepository = Mockito.mock(PaymentConceptsRepository.class);
        PaymentConceptsServiceImpl paymentConceptsService = new PaymentConceptsServiceImpl(paymentConceptsRepository);

        org.uresti.pozarreal.model.PaymentConcept paymentConcept = org.uresti.pozarreal.model.PaymentConcept.builder()
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentConceptsRepository.save(paymentConcept)).thenReturn(paymentConcept);

        //When:
        PaymentConcept paymentConceptSaved = paymentConceptsService.update(PaymentConceptMapper.entityToDto(paymentConcept));

        //Then:
        Assertions.assertThat(paymentConceptSaved.getId()).isEqualTo("id");
        Assertions.assertThat(paymentConceptSaved.getLabel()).isEqualTo("label");
    }

    @Test
    public void givenAnPaymentConceptWithPaymentConceptIdNull_whenUpdate_thenPaymentConceptThrowBadRequestDataException() {
        //Given:
        PaymentConceptsRepository paymentConceptsRepository = Mockito.mock(PaymentConceptsRepository.class);
        PaymentConceptsServiceImpl paymentConceptsService = new PaymentConceptsServiceImpl(paymentConceptsRepository);

        org.uresti.pozarreal.model.PaymentConcept paymentConcept = org.uresti.pozarreal.model.PaymentConcept.builder()
                .label("label")
                .build();

        Mockito.when(paymentConceptsRepository.save(paymentConcept)).thenReturn(paymentConcept);

        //When:
        //Then:
        Assertions.assertThatThrownBy(() -> paymentConceptsService.update(PaymentConceptMapper.entityToDto(paymentConcept)))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Missing payment concept id", "REQUIRED_PAYMENT_CONCEPT_ID");

        Mockito.verifyNoMoreInteractions(paymentConceptsRepository);
    }
}
