package org.uresti.pozarreal.testmode.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.model.User;
import org.uresti.pozarreal.repository.RolesRepository;
import org.uresti.pozarreal.service.UserService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@Profile("tests")
public class TestSystemUserDetailsService {

    private final UserService userService;
    private final RolesRepository rolesRepository;

    public TestSystemUserDetailsService(UserService userService,
                                        RolesRepository rolesRepository) {
        this.userService = userService;
        this.rolesRepository = rolesRepository;
    }

    public Authentication loadUser(String name, String password) {

        Map<String, Object> attributes = new HashMap<>();
        Set<GrantedAuthority> authorities = new HashSet<>();
        String email = name + "@" + password + ".com";
        String picture = "https://upload.wikimedia.org/wikipedia/commons/e/ec/Happy_smiley_face.png";

        attributes.put("email", email);
        attributes.put("picture", picture);
        attributes.put("name", name);

        User user = userService.findOrRegister(email, picture, name);


        rolesRepository.findRolesByUser(user.getId()).forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role)));


        attributes.put("userId", user.getId());

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(attributes)
                .grantedAuthorities(authorities)
                .name(user.getName())
                .build();

        return new UsernamePasswordAuthenticationToken(loggedUser, user, authorities);
    }
}
