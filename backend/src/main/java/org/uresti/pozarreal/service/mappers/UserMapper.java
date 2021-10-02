package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.User;

public class UserMapper {
    public static User entityToDto(org.uresti.pozarreal.model.User userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .picture(userEntity.getPicture())
                .build();
    }
}
