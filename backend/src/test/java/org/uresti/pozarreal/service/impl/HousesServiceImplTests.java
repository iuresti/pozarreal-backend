package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.PaymentByConcept;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.HouseByUser;
import org.uresti.pozarreal.model.Payment;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

public class HousesServiceImplTests {

    @Test
    public void givenAnNullStringId_whenGetHousesByStreet_thenReturnEmptyList() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        LoggedUser loggedUser = LoggedUser.builder().build();

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        // When:
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);

        // Then:
        List<House> housesByStreet = housesService.getHousesByStreet("123", loggedUser);
        Assertions.assertThat(housesByStreet).isEmpty();
    }

    @Test
    public void givenAnHousesWithOneElement_whenGetHousesByStreet_thenReturnListWithOneElement() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        LoggedUser loggedUser = LoggedUser.builder().build();

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        org.uresti.pozarreal.model.House house = org.uresti.pozarreal.model.House.builder()
                .id("1")
                .notes("hello")
                .street("Street 1")
                .number("123456")
                .chipsEnabled(true)
                .build();

        houses.add(house);

        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);

        // When:
        List<House> housesByStreet = housesService.getHousesByStreet("123", loggedUser);

        // Then:
        Assertions.assertThat(housesByStreet.size()).isEqualTo(1);
        Assertions.assertThat(housesByStreet.get(0).getId()).isEqualTo("1");
        Assertions.assertThat(housesByStreet.get(0).getStreet()).isEqualTo("Street 1");
        Assertions.assertThat(housesByStreet.get(0).getNumber()).isEqualTo("123456");
    }

    @Test
    public void givenNotEqualStreetIdAndStreet_whenGetHousesByStreet_thenThrowBadRequestDataException() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String userId = UUID.randomUUID().toString();
        LoggedUser user = LoggedUser.builder().claims(Collections.singletonMap("userId", userId)).build();

        org.uresti.pozarreal.model.Representative representative = org.uresti.pozarreal.model.Representative.builder()
                .street("Street 2")
                .house("house 1")
                .phone("123456")
                .userId("userId1")
                .build();

        Mockito.when(sessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(sessionHelper.hasRole(user, Role.ROLE_ADMIN)).thenReturn(false);
        Mockito.when(representativeRepository.findById(userId)).thenReturn(java.util.Optional.of(representative));

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> housesService.getHousesByStreet("Street 1", user))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Invalid street for representative query", "INVALID_STREET");
    }

    @Test
    public void givenAnHouseId_wheToggleChipStatusRequest_thenEnableChip() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        org.uresti.pozarreal.model.House house = org.uresti.pozarreal.model.House.builder()
                .id("1")
                .notes("hello")
                .street("Street 1")
                .number("123456")
                .chipsEnabled(false)
                .build();

        Mockito.when(housesRepository.save(house)).thenReturn(house);
        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));

        // When:
        housesService.toggleChipStatusRequest("1", false);

        // Then:
        Mockito.verify(housesRepository).save(house);
        Mockito.verify(housesRepository).findById("1");
    }

    @Test
    public void givenAnCorrectHouseId_whenGetHouseInfo_thenReturnHouseInfo() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        org.uresti.pozarreal.model.House house = org.uresti.pozarreal.model.House.builder()
                .id("1")
                .notes("hello")
                .street("Street 1")
                .number("123456")
                .chipsEnabled(true)
                .build();

        Street street = Street.builder()
                .id("1")
                .name("name 1")
                .build();

        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));
        Mockito.when(streetRepository.findById(house.getStreet())).thenReturn(java.util.Optional.of(street));

        // When:
        HouseInfo houseInfo = housesService.getHouseInfo("1");

        // Then:
        Assertions.assertThat(houseInfo).isNotNull();
        Assertions.assertThat(houseInfo.getId()).isEqualTo("1");
        Assertions.assertThat(houseInfo.getNotes()).isEqualTo("hello");
        Assertions.assertThat(houseInfo.getStreet()).isEqualTo("Street 1");
        Assertions.assertThat(houseInfo.getStreetName()).isEqualTo("name 1");
        Assertions.assertThat(houseInfo.getNumber()).isEqualTo("123456");
        Assertions.assertThat(houseInfo.isChipsEnabled()).isTrue();
    }

    @Test
    public void givenStringIdCorrect_whenGetNotes_thenSaveNotes() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        org.uresti.pozarreal.model.House house = org.uresti.pozarreal.model.House.builder()
                .id("1")
                .notes("hello")
                .street("Street 1")
                .number("123456")
                .chipsEnabled(false)
                .build();

        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));
        Mockito.when(housesRepository.save(house)).thenReturn(house);

        // When:
        housesService.saveNotes("1", "hello");

        // Then:
        Mockito.verify(housesRepository).save(house);
        Mockito.verify(housesRepository).findById("1");
    }

    @Test
    public void givenAnEmptyHousesByUserList_whenGetHousesByUser_thenEmptyListIsReturned() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        List<HouseByUser> houseByUsers = new LinkedList<>();

        String id = UUID.randomUUID().toString();

        Mockito.when(housesByUserRepository.findAllByUserId(id)).thenReturn(houseByUsers);

        // When:
        List<org.uresti.pozarreal.dto.HouseByUser> housesReturned = housesService.getHousesByUser(id);

        // Then:
        Assertions.assertThat(housesReturned).isNotNull();
        Assertions.assertThat(housesReturned).isEmpty();
    }

    @Test
    public void givenAHouseByUserListWithTwoElements_whenGetHousesByUser_thenListIsReturnedWithTwoElements() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String id = UUID.randomUUID().toString();

        HouseByUser house1 = HouseByUser.builder()
                .id("id1")
                .userId(id)
                .build();

        HouseByUser house2 = HouseByUser.builder()
                .id("id2")
                .userId(id)
                .build();

        List<HouseByUser> houseByUsers = new LinkedList<>();

        houseByUsers.add(house1);
        houseByUsers.add(house2);

        Mockito.when(housesByUserRepository.findAllByUserId(id)).thenReturn(houseByUsers);

        // When:
        List<org.uresti.pozarreal.dto.HouseByUser> housesReturned = housesService.getHousesByUser(id);

        // Then:
        Assertions.assertThat(housesReturned).isNotNull();
        Assertions.assertThat(housesReturned.size()).isEqualTo(2);
        Assertions.assertThat(housesReturned.get(0).getUserId()).isEqualTo(id);
        Assertions.assertThat(housesReturned.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(housesReturned.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(housesReturned.get(1).getUserId()).isEqualTo(id);
    }

    @Test
    public void givenId_whenDeleteHouseByUser_thenHouseByUserIsDeleted() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String id = UUID.randomUUID().toString();

        // When:
        housesService.deleteHouseByUser(id);

        // Then:
        Mockito.verify(housesByUserRepository).deleteById(id);
    }

    @Test
    public void givenPrincipalParameter_whenGetHousesByUser_ThenListIsReturned() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Principal principal = Mockito.mock(Principal.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String id = UUID.randomUUID().toString();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("id", id))
                .build();

        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        List<org.uresti.pozarreal.dto.HouseByUser> houseByUsers = housesService.getHousesByUser(principal);

        // Then:
        Assertions.assertThat(houseByUsers).isNotNull();
        Assertions.assertThat(houseByUsers).isEmpty();
    }

    @Test
    public void givenHouseByUserNull_whenSaveHouseByUser_thenBadRequestDataExceptionIsThrown() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        org.uresti.pozarreal.dto.HouseByUser houseByUser = org.uresti.pozarreal.dto.HouseByUser.builder().build();

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> housesService.saveHouseByUser(houseByUser))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("it's no possible save null house");

    }

    @Test
    public void givenAHouseByUser_whenSaveHouseByUser_thenHouseByUserIsSaved() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String houseId = UUID.randomUUID().toString();

        List<HouseByUser> houseByUsers = new LinkedList<>();

        HouseByUser houseByUser = HouseByUser.builder()
                .houseId(houseId)
                .id("id")
                .userId("userId")
                .validated(true)
                .mainHouse(true)
                .build();

        org.uresti.pozarreal.dto.HouseByUser houseByUserDto = org.uresti.pozarreal.dto.HouseByUser.builder()
                .houseId(houseId)
                .build();

        ArgumentCaptor<HouseByUser> houseByUserArgumentCaptor = ArgumentCaptor.forClass(HouseByUser.class);

        Mockito.when(housesByUserRepository.findAllByUserId(houseByUser.getUserId())).thenReturn(houseByUsers);
        Mockito.when(housesByUserRepository.save(houseByUserArgumentCaptor.capture())).thenReturn(houseByUser);

        // When:
        org.uresti.pozarreal.dto.HouseByUser houseByUserSaved = housesService.saveHouseByUser(houseByUserDto);

        HouseByUser parameter = houseByUserArgumentCaptor.getValue();

        // Then:
        Assertions.assertThat(parameter).isNotNull();
        Assertions.assertThat(parameter.getValidated()).isFalse();
        Assertions.assertThat(parameter.getMainHouse()).isFalse();
        Assertions.assertThat(parameter.getId()).isNotNull();
        Assertions.assertThat(parameter.getId()).isNotEqualTo(houseByUserSaved.getId());
        Assertions.assertThat(parameter.getHouseId()).isEqualTo(houseId);

        Assertions.assertThat(houseByUserSaved.getValidated()).isTrue();
        Assertions.assertThat(houseByUserSaved.getMainHouse()).isTrue();
        Assertions.assertThat(houseByUserSaved.getId()).isEqualTo("id");
        Assertions.assertThat(houseByUserSaved.getUserId()).isEqualTo("userId");
        Assertions.assertThat(houseByUserSaved.getHouseId()).isEqualTo(houseId);
    }

    @Test
    public void givenADuplicateHouseByUser_whenSaveHouseByUser_thenBadRequestDataExceptionIsThrown() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String houseId = UUID.randomUUID().toString();

        org.uresti.pozarreal.dto.HouseByUser houseByUserToSave = org.uresti.pozarreal.dto.HouseByUser.builder()
                .houseId(houseId)
                .id("id1")
                .userId("userId")
                .validated(true)
                .mainHouse(true)
                .build();

        HouseByUser houseByUser1 = HouseByUser.builder()
                .houseId("houseId1")
                .id("id1")
                .userId("userId")
                .validated(true)
                .mainHouse(true)
                .build();

        HouseByUser houseByUser2 = HouseByUser.builder()
                .houseId(houseId)
                .id("id2")
                .userId("userId")
                .validated(true)
                .mainHouse(true)
                .build();

        List<HouseByUser> houseByUsers = new LinkedList<>();
        houseByUsers.add(houseByUser1);
        houseByUsers.add(houseByUser2);

        Mockito.when(housesByUserRepository.findAllByUserId(houseByUserToSave.getUserId())).thenReturn(houseByUsers);

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> housesService.saveHouseByUser(houseByUserToSave))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("already you are owner of this house");
    }

    @Test
    public void givenAHouseId_whenGetPaymentsHouse_thenGetTwoMonthsPayments() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper sessionHelper = Mockito.mock(SessionHelper.class);
        HousesByUserRepository housesByUserRepository = Mockito.mock(HousesByUserRepository.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

        HousesServiceImpl housesService = new HousesServiceImpl(
                housesRepository,
                streetRepository,
                representativeRepository,
                sessionHelper,
                housesByUserRepository,
                paymentRepository);

        String houseId = UUID.randomUUID().toString();
        LocalDate startOfYear = LocalDate.now().withYear(LocalDate.now().getYear()).withDayOfYear(1);
        LocalDate endOfYear = LocalDate.now().withYear(LocalDate.now().getYear() + 1).withDayOfYear(1);

        Payment payment1 = Payment.builder()
                .id("id1")
                .amount(200.0)
                .houseId(houseId)
                .build();

        Payment payment2 = Payment.builder()
                .id("id1")
                .amount(200.0)
                .houseId(houseId)
                .build();

        List<Payment> payments = new LinkedList<>();
        payments.add(payment1);
        payments.add(payment2);

        Mockito.when(paymentRepository.findAllByHouseIdAndPaymentDateBetween(houseId, startOfYear, endOfYear)).thenReturn(payments);

        // When:
        ArrayList<PaymentByConcept> paymentByConcepts = housesService.getPaymentsHouse(houseId);

        // Then:
        Assertions.assertThat(paymentByConcepts).isNotNull();
    }
}