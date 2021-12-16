package org.uresti.pozarreal.service;

import java.util.List;

import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.model.Street;

public interface StreetsService {
    List<Street> getStreets(LoggedUser user);

    StreetInfo getStreetInfo(String streetId, LoggedUser userLogged, String startYear, String endYear);
}
