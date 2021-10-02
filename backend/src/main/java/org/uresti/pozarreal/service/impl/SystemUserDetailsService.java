package org.uresti.pozarreal.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
@Profile("!tests")
public class SystemUserDetailsService {

    private final RolesRepository rolesRepository;
    private final UserService userService;

    public SystemUserDetailsService(RolesRepository rolesRepository,
                                    UserService userService) {
        this.rolesRepository = rolesRepository;
        this.userService = userService;
    }

    public OidcUser loadUser(OidcUserRequest userRequest) {
        Map<String, Object> attributes = new HashMap<>(userRequest.getIdToken().getClaims());
        String email = (String) attributes.get("email");
        String picture = (String) attributes.get("picture");
        String name = (String) attributes.get("name");
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        User user = userService.findOrRegister(email, picture, name);

        rolesRepository.findRolesByUser(user.getId()).forEach(role ->
                mappedAuthorities.add(new SimpleGrantedAuthority(role)));

        OidcUserInfo userInfo = new OidcUserInfo(attributes);

        attributes.put("userId", user.getId());

        return LoggedUser.builder()
                .oidcIdToken(userRequest.getIdToken())
                .claims(attributes)
                .grantedAuthorities(mappedAuthorities)
                .oidcUserInfo(userInfo)
                .name(user.getName())
                .build();
    }
}
