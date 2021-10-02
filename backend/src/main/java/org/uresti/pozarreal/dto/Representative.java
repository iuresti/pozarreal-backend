package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Representative {

    private String userId;
    private String name;
    private String street;
    private String phone;
    private String house;
    private String houseNumber;
    private String streetName;

}
