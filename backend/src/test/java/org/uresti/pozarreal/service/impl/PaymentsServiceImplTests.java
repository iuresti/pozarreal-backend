package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.exception.MaintenanceFeeOverPassedException;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.repository.CustomPaymentRepository;
import org.uresti.pozarreal.repository.PaymentRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentsServiceImplTests {

    @Test
    public void givenPaymentViewListEmpty_whenGetPayments_thenReturnPaymentViewListEmpty() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        List<PaymentView> paymentViews = new LinkedList<>();

        Pageable pageable = PageRequest.of(0, 2);

        PageImpl<PaymentView> paymentViewPage = new PageImpl<>(paymentViews, pageable, 0);

        PaymentFilter paymentFilter = PaymentFilter.builder().build();

        Mockito.when(customPaymentRepository.executeQuery(paymentFilter, 0)).thenReturn(paymentViewPage);

        // When:
        Page<PaymentView> paymentViewList = paymentsService.getPayments(paymentFilter, 0);

        // Then:
        Assertions.assertThat(paymentViewList.isEmpty()).isTrue();
    }

    @Test
    public void givenPaymentViewListWithTwoElements_whenGetPayments_thenReturnPaymentViewListWithTwoElements() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        List<PaymentView> paymentViews = new LinkedList<>();

        PaymentView paymentView1 = PaymentView.builder()
                .id("id1")
                .houseId("house1")
                .amount(100.0)
                .notes("hello")
                .build();

        PaymentView paymentView2 = PaymentView.builder()
                .id("id2")
                .houseId("house2")
                .amount(100.0)
                .notes("hello")
                .build();

        paymentViews.add(paymentView1);
        paymentViews.add(paymentView2);

        Pageable pageable = PageRequest.of(0, 2);

        Page<PaymentView> paymentViewPage = new PageImpl<>(paymentViews, pageable, 20);

        PaymentFilter paymentFilter = PaymentFilter.builder().build();

        Mockito.when(customPaymentRepository.executeQuery(paymentFilter, 0)).thenReturn(paymentViewPage);

        // When:
        Page<PaymentView> paymentViewList = paymentsService.getPayments(paymentFilter, 0);

        // Then:
        Assertions.assertThat((int) paymentViewList.get().count()).isEqualTo(2);
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(0).getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(0).getAmount()).isEqualTo(100.0);
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(0).getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(1).getHouseId()).isEqualTo("house2");
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(1).getAmount()).isEqualTo(100.0);
        Assertions.assertThat(paymentViewList.get().collect(Collectors.toList()).get(1).getNotes()).isEqualTo("hello");
    }

    @Test
    public void givenAPayment_whenSave_thenSave() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);
        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor =
                ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(true)
                .amount(100.0)
                .id("id1")
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Payment paymentDto = Payment.builder()
                .amount(100.0)
                .id("id2")
                .houseId("house2")
                .notes("hello")
                .paymentConceptId("Concept2")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);

        // When:
        Payment paymentSaved = paymentsService.save(paymentDto, principal);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentSaved.getId()).isEqualTo("id1");
        Assertions.assertThat(paymentSaved.getAmount()).isEqualTo(100);
        Assertions.assertThat(paymentSaved.isValidated()).isTrue();
        Assertions.assertThat(paymentSaved.getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentSaved.getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentSaved.getPaymentConceptId()).isEqualTo("Concept");
        Assertions.assertThat(paymentSaved.getPaymentMode()).isEqualTo("mode");
        Assertions.assertThat(paymentSaved.getPaymentSubConceptId()).isEqualTo("SubConcept");
        Assertions.assertThat(paymentSaved.getUserId()).isEqualTo("user1");

        Assertions.assertThat(parameter.getRegistrationDate()).isNotEqualTo(registrationDate);
    }

    @Test
    public void givenAPaymentWithIdIsNull_whenSave_thenSaveAndSetId() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);

        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor =
                ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        String userId = UUID.randomUUID().toString();
        LoggedUser loggedUser = LoggedUser.builder().claims(Collections.singletonMap("userId", userId)).build();

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(false)
                .amount(100.0)
                .id("id1")
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Payment paymentDto = Payment.builder()
                .validated(false)
                .amount(100.0)
                .houseId("house2")
                .notes("hello")
                .paymentConceptId("Concept2")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);
        Mockito.when(sessionHelper.getUserIdForLoggedUser(principal)).thenReturn(String.valueOf(loggedUser));

        // When:
        Payment paymentSaved = paymentsService.save(paymentDto, principal);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentSaved.getId()).isNotNull();
        Assertions.assertThat(paymentSaved.getAmount()).isEqualTo(100);
        Assertions.assertThat(paymentSaved.isValidated()).isFalse();
        Assertions.assertThat(paymentSaved.getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentSaved.getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentSaved.getPaymentConceptId()).isEqualTo("Concept");
        Assertions.assertThat(paymentSaved.getPaymentMode()).isEqualTo("mode");
        Assertions.assertThat(paymentSaved.getPaymentSubConceptId()).isEqualTo("SubConcept");
        Assertions.assertThat(paymentSaved.getUserId()).isEqualTo("user1");

        Assertions.assertThat(parameter.getRegistrationDate()).isNotEqualTo(registrationDate);
        Assertions.assertThat(parameter.getId()).isNotEqualTo(userId);
    }

    @Test
    public void givenAValidatedPayment_whenSave_ThenThrowException() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        Payment paymentDto = Payment.builder()
                .validated(true)
                .amount(100.0)
                .houseId("house2")
                .notes("hello")
                .paymentConceptId("Concept2")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.save(paymentDto, principal))
                .isInstanceOf(PozarrealSystemException.class);
    }

    @Test
    public void givenAPaymentConceptIsEqualToMaintenance_whenSave_thenValidateMaintenancePayment() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);
        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor =
                ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setBiMonthlyMaintenanceFee(100.0);
        feeConfig.setParkingPenFee(50.0);
        feeConfig.setYearlyMaintenanceFee(200.0);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();
        pozarrealConfig.setFees(feeConfig);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(true)
                .amount(100.0)
                .id("id1")
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("MAINTENANCE")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Payment paymentDto = Payment.builder()
                .amount(100.0)
                .id("id2")
                .houseId("house2")
                .notes("hello")
                .registrationDate(paymentDate)
                .paymentConceptId("MAINTENANCE")
                .paymentDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);

        // When:
        Payment paymentSaved = paymentsService.save(paymentDto, principal);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentSaved.getId()).isNotNull();
        Assertions.assertThat(paymentSaved.getAmount()).isEqualTo(100);
        Assertions.assertThat(paymentSaved.isValidated()).isTrue();
        Assertions.assertThat(paymentSaved.getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentSaved.getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentSaved.getPaymentConceptId()).isEqualTo("MAINTENANCE");
        Assertions.assertThat(paymentSaved.getPaymentMode()).isEqualTo("mode");
        Assertions.assertThat(paymentSaved.getPaymentSubConceptId()).isEqualTo("SubConcept");
        Assertions.assertThat(paymentSaved.getUserId()).isEqualTo("user1");

        Assertions.assertThat(parameter.getRegistrationDate()).isNotEqualTo(registrationDate);
    }

    @Test
    public void givenAPaymentConceptIsEqualToMaintenanceAndAmountGreaterThanPozarrealFees_whenSave_thenValidateMaintenancePaymentAndThrowMaintenanceFeeOverPassedException() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);

        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setBiMonthlyMaintenanceFee(100.0);
        feeConfig.setParkingPenFee(50.0);
        feeConfig.setYearlyMaintenanceFee(200.0);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();
        pozarrealConfig.setFees(feeConfig);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        Payment paymentDto = Payment.builder()
                .amount(200.0)
                .id("id2")
                .houseId("house2")
                .notes("hello")
                .registrationDate(paymentDate)
                .paymentConceptId("MAINTENANCE")
                .paymentDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.save(paymentDto, principal))
                .isInstanceOf(MaintenanceFeeOverPassedException.class)
                .hasMessage("Over passed maintenance fee", "ERROR_OVER_PASS_PAYMENT");
    }

    @Test
    public void givenAPaymentConceptAndUserIsAdmin_whenSave_thenValidaPaymentAndSave() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor =
                ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(false)
                .amount(200.0)
                .id("id1")
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Payment paymentDto = Payment.builder()
                .validated(false)
                .id("id2")
                .amount(200.0)
                .houseId("house2")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept2")
                .userId("user2")
                .build();

        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);
        Mockito.when(sessionHelper.hasRole(sessionHelper.getLoggedUser(principal), Role.ROLE_ADMIN)).thenReturn(true);

        // When:
        Payment paymentSaved = paymentsService.save(paymentDto, principal);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(paymentSaved).isNotNull();
        Assertions.assertThat(parameter.isValidated()).isTrue();
        Assertions.assertThat(parameter.isValidated()).isNotEqualTo(paymentSaved.isValidated());
        Assertions.assertThat(paymentSaved.getUserId()).isEqualTo("user1");
        Assertions.assertThat(paymentSaved.getAmount()).isEqualTo(200.0);
        Assertions.assertThat(paymentSaved.getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentSaved.getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentSaved.getPaymentConceptId()).isEqualTo("Concept");
        Assertions.assertThat(paymentSaved.getPaymentDate()).isEqualTo(paymentDate);
        Assertions.assertThat(paymentSaved.getRegistrationDate()).isEqualTo(registrationDate);
        Assertions.assertThat(paymentSaved.getPaymentMode()).isEqualTo("mode");
        Assertions.assertThat(paymentSaved.getPaymentSubConceptId()).isEqualTo("SubConcept");
        Assertions.assertThat(paymentSaved.getId()).isEqualTo("id1");
    }

    @Test
    public void givenAPaymentId_whenDelete_thenDelete() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        Principal principal = Mockito.mock(Principal.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        String id = UUID.randomUUID().toString();

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(false)
                .amount(200.0)
                .id(id)
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Mockito.when(sessionHelper.hasRole(sessionHelper.getLoggedUser(principal), Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(paymentRepository.findById(id)).thenReturn(Optional.ofNullable(payment));

        // When:
        paymentsService.delete(id, principal);

        // Then:
        Mockito.verify(paymentRepository).deleteById(id);
    }

    @Test
    public void givenAPaymentValidated_whenDelete_thenPozarrealSystemExceptionIsThrown() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);
        Principal principal = Mockito.mock(Principal.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        String id = UUID.randomUUID().toString();

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(true)
                .amount(200.0)
                .id(id)
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Mockito.when(paymentRepository.findById(id)).thenReturn(Optional.ofNullable(payment));

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.delete(id, principal))
                .isInstanceOf(PozarrealSystemException.class)
                .hasMessage("Deleting payment validated");
    }

    @Test
    public void givenAPaymentIdAndUserId_whenGetPayment_thenReturnPayment() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        LocalDate paymentDate = LocalDate.now();
        LocalDate registrationDate = LocalDate.ofYearDay(2000, 10);

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .validated(false)
                .amount(200.0)
                .id("id")
                .houseId("house1")
                .notes("hello")
                .paymentConceptId("Concept")
                .paymentDate(paymentDate)
                .registrationDate(registrationDate)
                .paymentMode("mode")
                .paymentSubConceptId("SubConcept")
                .userId("user1")
                .build();

        Mockito.when(paymentRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(payment));

        // When:
        Payment paymentObtained = paymentsService.getPayment("id", null);

        // Then:
        Assertions.assertThat(paymentObtained).isNotNull();
        Assertions.assertThat(paymentObtained.getId()).isEqualTo("id");
        Assertions.assertThat(paymentObtained.getAmount()).isEqualTo(200.0);
        Assertions.assertThat(paymentObtained.isValidated()).isFalse();
        Assertions.assertThat(paymentObtained.getHouseId()).isEqualTo("house1");
        Assertions.assertThat(paymentObtained.getNotes()).isEqualTo("hello");
        Assertions.assertThat(paymentObtained.getPaymentConceptId()).isEqualTo("Concept");
        Assertions.assertThat(paymentObtained.getPaymentDate()).isEqualTo(paymentDate);
        Assertions.assertThat(paymentObtained.getRegistrationDate()).isEqualTo(registrationDate);
        Assertions.assertThat(paymentObtained.getPaymentMode()).isEqualTo("mode");
        Assertions.assertThat(paymentObtained.getPaymentSubConceptId()).isEqualTo("SubConcept");
        Assertions.assertThat(paymentObtained.getUserId()).isEqualTo("user1");
    }

    @Test
    public void givenAPaymentIdNoExistent_whenGetPayment_thenThrowNoSuchElementException() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.getPayment("id", null))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void givenAPaymentId_whenValidatePayment_thenUpdatePaymentWithValidateFlag() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor = ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        String paymentId = UUID.randomUUID().toString();

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .amount(100.0)
                .id(paymentId)
                .validated(false)
                .build();


        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.ofNullable(payment));
        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);

        // When:
        Payment validatePayment = paymentsService.validatePayment(paymentId);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(validatePayment).isNotNull();
        Assertions.assertThat(parameter).isNotNull();
        Assertions.assertThat(parameter.isValidated()).isTrue();
        Assertions.assertThat(validatePayment.getAmount()).isEqualTo(100.0);
        Assertions.assertThat(parameter.getAmount()).isEqualTo(100.0);
        Assertions.assertThat(parameter.getId()).isEqualTo(paymentId);
        Assertions.assertThat(validatePayment.getId()).isEqualTo(paymentId);
    }

    @Test
    public void givenAnNoExistentPaymentId_whenValidatePayment_theThrowNoSuchElementException() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.validatePayment(null))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void givenAPaymentId_whenConflictPayment_thenUpdatePaymentWithConflictFlag() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        ArgumentCaptor<org.uresti.pozarreal.model.Payment> argumentCaptor = ArgumentCaptor.forClass(org.uresti.pozarreal.model.Payment.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        String paymentId = UUID.randomUUID().toString();

        org.uresti.pozarreal.model.Payment payment = org.uresti.pozarreal.model.Payment.builder()
                .amount(100.0)
                .id(paymentId)
                .conflict(false)
                .build();


        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.ofNullable(payment));
        Mockito.when(paymentRepository.save(argumentCaptor.capture())).thenReturn(payment);

        // When:
        Payment conflictPayment = paymentsService.conflictPayment(paymentId);

        // Then:
        org.uresti.pozarreal.model.Payment parameter = argumentCaptor.getValue();

        Assertions.assertThat(conflictPayment).isNotNull();
        Assertions.assertThat(parameter).isNotNull();
        Assertions.assertThat(parameter.isConflict()).isTrue();
        Assertions.assertThat(conflictPayment.getAmount()).isEqualTo(100.0);
        Assertions.assertThat(parameter.getAmount()).isEqualTo(100.0);
        Assertions.assertThat(parameter.getId()).isEqualTo(paymentId);
        Assertions.assertThat(conflictPayment.getId()).isEqualTo(paymentId);
    }

    @Test
    public void givenAnNoExistentPaymentId_whenConflictPayment_theThrowNoSuchElementException() {
        // Given:
        CustomPaymentRepository customPaymentRepository = Mockito.mock(CustomPaymentRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PozarrealConfig pozarrealConfig = Mockito.mock(PozarrealConfig.class);

        PaymentsServiceImpl paymentsService = new PaymentsServiceImpl(
                customPaymentRepository,
                paymentRepository,
                sessionHelper,
                pozarrealConfig);

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> paymentsService.conflictPayment(null))
                .isInstanceOf(NoSuchElementException.class);
    }
}
