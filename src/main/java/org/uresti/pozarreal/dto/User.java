package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    Login logins;
    String name;
    String picture;
    List<String> roles;
    House house;
    String status;
    List<House> additionalHouses;
}
