package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Login;

import java.util.List;

public class LoginMapper {
    public static Login buildLoginInfo(List<org.uresti.pozarreal.model.Login> all) {
        Login login = new Login();

        login.setGoogle(true);

        return login;
    }
}
