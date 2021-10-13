package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseInfo {
    private String id;
    private String number;
    private boolean chipsEnabled;
    private String street;
    private String streetName;
    private String notes;
}
