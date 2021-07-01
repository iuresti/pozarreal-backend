package org.uresti.pozarreal.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.StreetRepository;

import java.util.LinkedList;
import java.util.List;

public class StreetServiceImplTests {

    @Test
    public void givenAnEmptyStreetList_whenGetStreets_thenEmptyListIsReturned() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = null;
        HousesRepository housesRepository = null;
        StreetServiceImpl streetService = new StreetServiceImpl(streetRepository, representativeRepository, housesRepository, null, null, null, null, null, null);
        LoggedUser user = LoggedUser.builder().build();

        List<Street> lista = new LinkedList<>();

        Mockito.when(streetRepository.findAll()).thenReturn(lista);

        // When:
        List<Street> streets = streetService.getStreets(user);

        // Then:
        Assertions.assertTrue(streets.isEmpty());
    }

    @Test
    public void givenAnStreetListWithTwoElements_whenGetStreets_thenListWithTwoElementsIsReturned() {
        // Given:
        StreetRepository streetRepository = Mockito.mock(StreetRepository.class);
        RepresentativeRepository representativeRepository = null;
        HousesRepository housesRepository = null;
        StreetServiceImpl streetService = new StreetServiceImpl(streetRepository, representativeRepository, housesRepository, null, null, null, null, null, null);
        LoggedUser user = LoggedUser.builder().build();

        List<Street> lista = new LinkedList<>();

        Street street1 = new Street();
        street1.setId("id1");
        street1.setName("Street 1");
        lista.add(street1);

        Street street2 = new Street();
        street2.setId("id2");
        street2.setName("Street 2");
        lista.add(street2);

        Mockito.when(streetRepository.findAll()).thenReturn(lista);


        // When:
        List<Street> streets = streetService.getStreets(user);

        // Then:
        Assertions.assertEquals(2, streets.size());
        Assertions.assertEquals("id1", streets.get(0).getId());
        Assertions.assertEquals("Street 1", streets.get(0).getName());
        Assertions.assertEquals("id2", streets.get(1).getId());
        Assertions.assertEquals("Street 2", streets.get(1).getName());
    }

}
