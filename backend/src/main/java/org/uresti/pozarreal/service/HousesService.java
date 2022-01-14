package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.HousesByUser;

import java.util.List;

public interface HousesService {
    void toggleChipStatusRequest(String houseId, boolean enable);

    List<House> getHousesByStreet(String streetId, LoggedUser user);

    List<HousesByUser> getHousesByUser(String userId);

    HouseInfo getHouseInfo(String houseId);

    void saveNotes(String houseId, String notes);

    void deleteHouseByUser(String id);

    HousesByUser saveHouseByUser(HousesByUser housesByUser);
}
