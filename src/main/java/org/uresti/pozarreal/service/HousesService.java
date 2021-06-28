package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.LoggedUser;

import java.util.List;

public interface HousesService {
    void toggleChipStatusRequest(String houseId, boolean enable);

    List<House> getHousesByStreet(String streetId, LoggedUser user);
}
