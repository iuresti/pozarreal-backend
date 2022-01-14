package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousesByUser {
    private String id;
    private String userId;
    private String houseId;
    private String streetName;
    private String number;
    private Boolean mainHouse;
    private Boolean validated;
}