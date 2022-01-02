package org.uresti.pozarreal.service;

public interface RepresentativesService {
    org.uresti.pozarreal.dto.Representative save(org.uresti.pozarreal.dto.Representative representative, String userId);

    void delete(String userId);

    org.uresti.pozarreal.dto.Representative saveStreet(String userId, String streetId);
}
