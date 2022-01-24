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
    private String id;
    private String name;
    private String picture;
    private List<String> roles;
    private Boolean status;
    private String street;
}
