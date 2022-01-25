package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public interface HousesService {
    void toggleChipStatusRequest(String houseId, boolean enable);

    List<House> getHousesByStreet(String streetId, LoggedUser user);

    List<HouseByUser> getHousesByUser(String userId);

    List<HouseByUser> getHousesByUser(Principal principal);

    HouseInfo getHouseInfo(String houseId);

    void saveNotes(String houseId, String notes);

    void deleteHouseByUser(String id);

    HouseByUser saveHouseByUser(HouseByUser houseByUser);

    ArrayList<PaymentByConcept> getPaymentsHouse(String houseId);
}
