HousesServiceImplTpackage org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.StreetRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class HousesServiceImplTests {

    @Test
    public void givenAnNullStringId_thenReturnEmptyList() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);
        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        // When:
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);

        // Then:
        List<House> housesByStreet = housesService.getHousesByStreet("123", mockSessionHelper.getLoggedUser(principal));
        Assertions.assertThat(housesByStreet).isEmpty();
    }

    @Test
    public void givenAHousesWithOneElement_whenGetHouses_thenReturnListWithOneElement() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        Principal principal = Mockito.mock(Principal.class);
        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        org.uresti.pozarreal.model.House house = new org.uresti.pozarreal.model.House();
        house.setId("1");
        house.setNotes("hello");
        house.setStreet("Street 1");
        house.setNumber("123456");
        house.setChipsEnabled(true);
        houses.add(house);

        // When:
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);

        // Then:
        List<House> housesByStreet = housesService.getHousesByStreet("123", mockSessionHelper.getLoggedUser(principal));

        Assertions.assertThat(housesByStreet.size()).isEqualTo(1);
        Assertions.assertThat(housesByStreet.get(0).getId()).isEqualTo("1");
        Assertions.assertThat(housesByStreet.get(0).getStreet()).isEqualTo("Street 1");
        Assertions.assertThat(housesByStreet.get(0).getNumber()).isEqualTo("123456");
    }

    @Test
    public void givenAnStreetId_whenRepresentativeStreetNotEqualsToStreetId_thenThrowBadRequestDataException() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);

        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        String userId = UUID.randomUUID().toString();
        LoggedUser user = LoggedUser.builder().claims(Collections.singletonMap("userId", userId)).build();

        org.uresti.pozarreal.model.Representative representative = org.uresti.pozarreal.model.Representative.builder()
                .street("Street 2")
                .house("house 1")
                .phone("123456")
                .userId("userId1")
                .build();

        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE)).thenReturn(true);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_ADMIN)).thenReturn(false);
        Mockito.when(representativeRepository.findById(userId)).thenReturn(java.util.Optional.of(representative));

        // When:
        // Then:
        Assertions.assertThatThrownBy(() -> housesService.getHousesByStreet("Street 1", user))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("Invalid street for representative query", "INVALID_STREET");
    }

    @Test
    public void givenAnHouseId_thenToggleChipStatusRequest() {
        // Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        org.uresti.pozarreal.model.House house = new org.uresti.pozarreal.model.House();
        house.setId("1");
        house.setNotes("hello");
        house.setStreet("Street 1");
        house.setNumber("123456");
        house.setChipsEnabled(true);

        Street street = new Street();

        street.setId("1");
        street.setName("name 1");

        Mockito.when(housesRepository.save(house)).thenReturn(house);
        Mockito.when(streetRepository.findById(house.getStreet())).thenReturn(java.util.Optional.of(street));
        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));

        // When:
        housesService.toggleChipStatusRequest("1", false);
        HouseInfo houseInfo = housesService.getHouseInfo("1");

        // Then:
        Assertions.assertThat(houseInfo.getNotes()).isEqualTo("hello");
        Assertions.assertThat(houseInfo.getStreet()).isEqualTo("Street 1");
        Assertions.assertThat(houseInfo.getNumber()).isEqualTo("123456");
        Assertions.assertThat(houseInfo.getId()).isEqualTo("1");
        Assertions.assertThat(houseInfo.isChipsEnabled()).isFalse();
    }

    @Test
    public void givenAnCorrectHouseId_thenReturnHouseInfo() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        org.uresti.pozarreal.model.House house = new org.uresti.pozarreal.model.House();
        house.setId("1");
        house.setNotes("hello");
        house.setStreet("123");
        house.setNumber("123456");
        house.setChipsEnabled(true);
        houses.add(house);

        Street street = new Street();

        street.setId("1");
        street.setName("name 1");

        // When:
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);
        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));
        Mockito.when(streetRepository.findById(house.getStreet())).thenReturn(java.util.Optional.of(street));

        // Then:
        HouseInfo houseInfo = housesService.getHouseInfo("1");

        Assertions.assertThat(houseInfo.getNotes()).isEqualTo("hello");
        Assertions.assertThat(houseInfo.getStreet()).isEqualTo("123");
        Assertions.assertThat(houseInfo.getNumber()).isEqualTo("123456");
        Assertions.assertThat(houseInfo.getStreetName()).isEqualTo("name 1");
        Assertions.assertThat(houseInfo.getId()).isEqualTo("1");
    }

    @Test
    public void givenStringIdCorrect_whenGetNotes_thenSaveNotes() {
        //Given:
        HousesRepository housesRepository = Mockito.mock(HousesRepository.class);
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = Mockito.mock(RepresentativeRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        HousesServiceImpl housesService = new HousesServiceImpl(housesRepository, streetRepository, representativeRepository, mockSessionHelper);

        List<org.uresti.pozarreal.model.House> houses = new LinkedList<>();

        org.uresti.pozarreal.model.House house = new org.uresti.pozarreal.model.House();
        house.setId("1");
        house.setStreet("123");
        house.setNumber("123456");
        house.setChipsEnabled(true);
        houses.add(house);

        Street street = new Street();

        street.setId("1");
        street.setName("name 1");

        // When:
        Mockito.when(housesRepository.findAllByStreetOrderByNumber("123")).thenReturn(houses);
        Mockito.when(housesRepository.findById("1")).thenReturn(java.util.Optional.of(house));
        Mockito.when(streetRepository.findById(house.getStreet())).thenReturn(java.util.Optional.of(street));

        // Then:
        housesService.saveNotes("1", "hello");
        HouseInfo houseInfo = housesService.getHouseInfo("1");

        Assertions.assertThat(houseInfo.getNotes()).isEqualTo("hello");
        Assertions.assertThat(houseInfo.getStreet()).isEqualTo("123");
        Assertions.assertThat(houseInfo.getNumber()).isEqualTo("123456");
        Assertions.assertThat(houseInfo.getStreetName()).isEqualTo("name 1");
        Assertions.assertThat(houseInfo.getId()).isEqualTo("1");
        Assertions.assertThat(houseInfo.isChipsEnabled()).isTrue();
    }
}