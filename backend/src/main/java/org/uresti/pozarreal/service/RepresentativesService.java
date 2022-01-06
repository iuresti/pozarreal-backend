package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.Representative;

public interface RepresentativesService {
    Representative save(Representative representative, String userId);

    void delete(String userId);

    Representative saveStreet(String userId, String streetId);
}
