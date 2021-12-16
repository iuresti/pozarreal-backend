package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.model.*;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.repository.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class StreetServiceImplTests {

    @Test
    public void givenAnEmptyStreetList_whenGetStreets_thenEmptyListIsReturned() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);
        LoggedUser loggedUser = LoggedUser.builder().build();

        List<Street> streets = new LinkedList<>();

        Mockito.when(streetRepository.findAll()).thenReturn(streets);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);

        // When:
        List<Street> streetList = streetService.getStreets(loggedUser);

        // Then:
        Assertions.assertThat(streetList.isEmpty()).isTrue();
    }

    @Test
    public void givenAnStreetListWithTwoElements_whenGetStreets_thenListWithTwoElementsIsReturned() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder().build();

        List<Street> streets = new LinkedList<>();

        Street street1 = Street.builder()
                .id("id1")
                .name("Street 1")
                .build();

        Street street2 = Street.builder()
                .id("id2")
                .name("Street 2")
                .build();

        streets.add(street1);
        streets.add(street2);

        Mockito.when(streetRepository.findAll()).thenReturn(streets);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);

        // When:
        List<Street> streetList = streetService.getStreets(loggedUser);

        // Then:
        Assertions.assertThat(streetList.size()).isEqualTo(2);
        Assertions.assertThat(streetList.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(streetList.get(0).getName()).isEqualTo("Street 1");
        Assertions.assertThat(streetList.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(streetList.get(1).getName()).isEqualTo("Street 2");
    }

    @Test
    public void givenAnLoggedUserWithOutAdminRoleAndWithRepresentativeRole_whenGetStreets_thenListWithOneElementIsReturned() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "name"))
                .build();

        Representative representative = Representative.builder()
                .userId("userId")
                .phone("12345")
                .street("street")
                .house("house")
                .build();

        Street street = Street.builder()
                .name("name")
                .id("id")
                .build();

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(false);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(representativeRepository.findById(loggedUser.getUserId())).thenReturn(java.util.Optional.ofNullable(representative));
        Mockito.when(streetRepository.findById("street")).thenReturn(java.util.Optional.ofNullable(street));

        // When:
        List<Street> streetList = streetService.getStreets(loggedUser);

        // Then:
        Assertions.assertThat(streetList).isNotNull();
        Assertions.assertThat(streetList.size()).isEqualTo(1);
        Assertions.assertThat(streetList.get(0).getId()).isEqualTo("id");
        Assertions.assertThat(streetList.get(0).getName()).isEqualTo("name");
    }

    @Test
    public void givenAnLoggedUserWithOutAdminRoleAndWithRepresentativeRole_whenGetStreets_thenThrowBadRequestDataException() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser user = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "name"))
                .build();

        Representative representative = Representative.builder()
                .userId("userId")
                .phone("12345")
                .house("house")
                .build();

        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_ADMIN)).thenReturn(false);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(representativeRepository.findById(user.getUserId())).thenReturn(java.util.Optional.ofNullable(representative));

        // When:
        // Then:
        assert representative != null;
        Assertions.assertThatThrownBy(() -> streetService.getStreets(user))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Wrong street configured for representative " + representative.getUserId(), "INVALID_STREET");
    }

    @Test
    public void givenAnStreetIdAndUserWithAdminRoleAndWithRepresentativeRole_whenGetStreetInfo_thenThrowBadRequestDataException() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "value"))
                .build();

        Representative representative = Representative.builder()
                .street("street")
                .userId("userId")
                .house("house")
                .phone("123456")
                .build();

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(false);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(representativeRepository.findById(loggedUser.getUserId())).thenReturn(Optional.ofNullable(representative));

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> streetService.getStreetInfo("street2", loggedUser, "2021", "2022"))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Invalid street for representative query", "INVALID_STREET");

        Mockito.verifyNoInteractions(streetRepository);
        Mockito.verifyNoInteractions(paymentRepository);
        Mockito.verifyNoInteractions(housesRepository);
    }

    @Test
    public void givenAndStreetId_whenGetStreetInfo_thenReturnStreetInfoWithEmptyHousesAndNullRepresentative() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "value"))
                .build();

        Street street = Street.builder()
                .name("name")
                .id("id")
                .build();

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(streetRepository.findById("street")).thenReturn(Optional.ofNullable(street));

        // When:
        StreetInfo streetInfo = streetService.getStreetInfo("street", loggedUser, "2021", "2022");

        // Then:
        Assertions.assertThat(streetInfo).isNotNull();
        Assertions.assertThat(streetInfo.getId()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getRepresentative()).isNull();
        Assertions.assertThat(streetInfo.getName()).isEqualTo("name");
        Assertions.assertThat(streetInfo.getHouses().isEmpty()).isTrue();
    }

    @Test
    public void givenAnStreetId_whenGetStreetInfo_thenIsReturnedStreetInfoWithRepresentativeAndEmptyHouses() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "value"))
                .build();

        User user = User.builder()
                .id("userId")
                .name("UserName")
                .picture("picture")
                .build();

        Street street = Street.builder()
                .name("name")
                .id("StreetId")
                .build();

        Representative representative = Representative.builder()
                .userId("userId")
                .phone("123456")
                .house("house")
                .street("streetId")
                .build();

        House house = House.builder()
                .number("number")
                .build();

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(streetRepository.findById("street")).thenReturn(Optional.ofNullable(street));
        Mockito.when(representativeRepository.findByStreet("StreetId")).thenReturn(Optional.ofNullable(representative));
        Mockito.when(userRepository.findById("userId")).thenReturn(Optional.ofNullable(user));
        Mockito.when(housesRepository.findById("house")).thenReturn(Optional.ofNullable(house));

        // When:
        StreetInfo streetInfo = streetService.getStreetInfo("street", loggedUser, "2021", "2022");

        // Then:
        Assertions.assertThat(streetInfo).isNotNull();
        Assertions.assertThat(streetInfo.getId()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getName()).isEqualTo("name");
        Assertions.assertThat(streetInfo.getHouses().isEmpty()).isTrue();
        Assertions.assertThat(streetInfo.getRepresentative()).isNotNull();
        Assertions.assertThat(streetInfo.getRepresentative().getUserId()).isEqualTo("userId");
        Assertions.assertThat(streetInfo.getRepresentative().getName()).isEqualTo("UserName");
        Assertions.assertThat(streetInfo.getRepresentative().getStreet()).isEqualTo("StreetId");
        Assertions.assertThat(streetInfo.getRepresentative().getPhone()).isEqualTo("123456");
        Assertions.assertThat(streetInfo.getRepresentative().getHouse()).isEqualTo("house");
        Assertions.assertThat(streetInfo.getRepresentative().getHouseNumber()).isEqualTo("number");
    }

    @Test
    public void givenAnStreetId_whenGetStreetInfo_thenIsReturnedStreetInfoWithOutRepresentativeAndHouses() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setYearlyMaintenanceFee(1000.0);
        feeConfig.setBiMonthlyMaintenanceFee(500.0);
        feeConfig.setParkingPenFee(100.0);

        pozarrealConfig.setFees(feeConfig);

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "value"))
                .build();

        Street street = Street.builder()
                .name("name")
                .id("StreetId")
                .build();

        LocalDate startOfYear = LocalDate.now().withDayOfYear(1).withYear(2021);
        LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(2022);

        LocalDate registrationDate = LocalDate.now();

        List<Payment> payments = new LinkedList<>();
        List<House> houses = new LinkedList<>();

        Payment payment = Payment.builder()
                .id("PaymentId")
                .validated(true)
                .paymentConceptId("PARKING_PEN")
                .houseId("houseId")
                .notes("hello")
                .paymentDate(startOfYear)
                .registrationDate(registrationDate)
                .paymentMode("PaymentMode")
                .paymentSubConceptId("MAINTENANCE_ANNUITY")
                .amount(100.0)
                .userId("UserId")
                .build();

        Payment payment2 = Payment.builder()
                .id("PaymentId2")
                .validated(false)
                .paymentConceptId("PARKING_PEN")
                .houseId("houseId")
                .paymentDate(startOfYear)
                .registrationDate(registrationDate)
                .notes("hello")
                .paymentMode("PaymentMode")
                .paymentSubConceptId("MAINTENANCE_ANNUITY")
                .amount(2000.0)
                .userId("UserId")
                .build();

        House house = House.builder()
                .chipsEnabled(true)
                .id("houseId")
                .number("12345")
                .street("street")
                .notes("hello")
                .build();

        payments.add(payment);
        payments.add(payment2);

        houses.add(house);

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(streetRepository.findById("street")).thenReturn(Optional.ofNullable(street));
        Mockito.when(paymentRepository.findAllByStreetAndPaymentDateBetween("street", startOfYear, endOfYear)).thenReturn(payments);
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("street")).thenReturn(houses);
        Mockito.when(paymentRepository.findAllByHouseIdAndPaymentConceptId("houseId", PaymentConcept.PARKING_PEN)).thenReturn(payments);

        // When:
        StreetInfo streetInfo = streetService.getStreetInfo("street", loggedUser, "2021", "2022");

        // Then:
        Assertions.assertThat(streetInfo).isNotNull();

        Assertions.assertThat(streetInfo.getRepresentative()).isNull();
        Assertions.assertThat(streetInfo.getId()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getName()).isEqualTo("name");

        Assertions.assertThat(streetInfo.getHouses().isEmpty()).isFalse();
        Assertions.assertThat(streetInfo.getHouses().size()).isEqualTo(1);
        Assertions.assertThat(streetInfo.getHouses().get(0).getStreet()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getHouses().get(0).getNumber()).isEqualTo("12345");
        Assertions.assertThat(streetInfo.getHouses().get(0).getId()).isEqualTo("houseId");
        Assertions.assertThat(streetInfo.getHouses().get(0).isChipsEnabled()).isTrue();
        Assertions.assertThat(streetInfo.getHouses().get(0).getId()).isEqualTo("houseId");
        Assertions.assertThat(streetInfo.getHouses().get(0).getStreet()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getHouses().get(0).getNumber()).isEqualTo("12345");

        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(0).isComplete()).isTrue();
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(1).isComplete()).isTrue();

        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().isValidated()).isFalse();
        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().getAmount()).isEqualTo(2100.0);
        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().isComplete()).isTrue();
    }

    @Test
    public void givenAnStreetIdAndPaymentSubConceptIsNotEqualsToMaintenanceAnnuity_whenGetStreetInfo_thenIsReturnedStreetInfoWithOutRepresentativeAndHouses() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setYearlyMaintenanceFee(1000.0);
        feeConfig.setBiMonthlyMaintenanceFee(500.0);
        feeConfig.setParkingPenFee(10.0);

        pozarrealConfig.setFees(feeConfig);

        StreetServiceImpl streetService = new StreetServiceImpl(
                streetRepository,
                representativeRepository,
                housesRepository,
                paymentRepository,
                userRepository,
                pozarrealConfig,
                mockSessionHelper);

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("key", "value"))
                .build();

        Street street = Street.builder()
                .name("name")
                .id("StreetId")
                .build();

        LocalDate startOfYear = LocalDate.now().withDayOfYear(1).withYear(2021);
        LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(2022);
        LocalDate registrationDate = LocalDate.now();

        List<Payment> payments = new LinkedList<>();
        List<House> houses = new LinkedList<>();

        Payment payment = Payment.builder()
                .id("PaymentId")
                .validated(true)
                .paymentConceptId("PARKING_PEN")
                .houseId("houseId")
                .notes("hello")
                .paymentDate(startOfYear)
                .registrationDate(registrationDate)
                .paymentMode("PaymentMode")
                .paymentSubConceptId("MAINTENANCE_BIM_1")
                .amount(100.0)
                .userId("UserId")
                .build();

        Payment payment2 = Payment.builder()
                .id("PaymentId2")
                .validated(false)
                .paymentConceptId("PARKING_PEN")
                .houseId("houseId")
                .paymentDate(startOfYear)
                .registrationDate(registrationDate)
                .notes("hello")
                .paymentMode("PaymentMode")
                .paymentSubConceptId("MAINTENANCE_BIM_2")
                .amount(2000.0)
                .userId("UserId")
                .build();

        House house = House.builder()
                .chipsEnabled(true)
                .id("houseId")
                .number("12345")
                .street("street")
                .notes("hello")
                .build();

        payments.add(payment);
        payments.add(payment2);

        houses.add(house);

        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_ADMIN)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(loggedUser, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(streetRepository.findById("street")).thenReturn(Optional.ofNullable(street));
        Mockito.when(paymentRepository.findAllByStreetAndPaymentDateBetween("street", startOfYear, endOfYear)).thenReturn(payments);
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("street")).thenReturn(houses);
        Mockito.when(paymentRepository.findAllByHouseIdAndPaymentConceptId("houseId", PaymentConcept.PARKING_PEN)).thenReturn(payments);

        // When:
        StreetInfo streetInfo = streetService.getStreetInfo("street", loggedUser, "2021", "2022");

        // Then:
        Assertions.assertThat(streetInfo).isNotNull();

        Assertions.assertThat(streetInfo.getRepresentative()).isNull();
        Assertions.assertThat(streetInfo.getId()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getName()).isEqualTo("name");

        Assertions.assertThat(streetInfo.getHouses().isEmpty()).isFalse();
        Assertions.assertThat(streetInfo.getHouses().size()).isEqualTo(1);
        Assertions.assertThat(streetInfo.getHouses().get(0).getStreet()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getHouses().get(0).getNumber()).isEqualTo("12345");
        Assertions.assertThat(streetInfo.getHouses().get(0).getId()).isEqualTo("houseId");
        Assertions.assertThat(streetInfo.getHouses().get(0).isChipsEnabled()).isTrue();
        Assertions.assertThat(streetInfo.getHouses().get(0).getId()).isEqualTo("houseId");
        Assertions.assertThat(streetInfo.getHouses().get(0).getStreet()).isEqualTo("street");
        Assertions.assertThat(streetInfo.getHouses().get(0).getNumber()).isEqualTo("12345");

        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(0).getId()).isEqualTo("PaymentId");
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(0).getAmount()).isEqualTo(100.0);
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(0).isValidated()).isTrue();
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(0).isComplete()).isFalse();

        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(1).getId()).isEqualTo("PaymentId2");
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(1).getAmount()).isEqualTo(2000.0);
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(1).isValidated()).isFalse();
        Assertions.assertThat(streetInfo.getHouses().get(0).getTwoMonthsPayments().get(1).isComplete()).isTrue();

        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().isValidated()).isFalse();
        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().getAmount()).isEqualTo(2100.0);
        Assertions.assertThat(streetInfo.getHouses().get(0).getParkingPenPayment().isComplete()).isTrue();
    }
}