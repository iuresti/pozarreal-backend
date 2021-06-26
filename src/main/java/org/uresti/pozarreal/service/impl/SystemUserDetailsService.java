package org.uresti.pozarreal.service.impl;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.model.Login;
import org.uresti.pozarreal.model.User;
import org.uresti.pozarreal.repository.LoginRepository;
import org.uresti.pozarreal.repository.RolesRepository;
import org.uresti.pozarreal.repository.UserRepository;

@Service
@Transactional
public class SystemUserDetailsService {

    private final LoginRepository loginRepository;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    public SystemUserDetailsService(LoginRepository loginRepository,
                                    UserRepository userRepository,
                                    RolesRepository rolesRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                    loadUserInfo(mappedAuthorities, oidcUserAuthority.getAttributes());

                } else if (authority instanceof OAuth2UserAuthority) {
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

                    loadUserInfo(mappedAuthorities, oauth2UserAuthority.getAttributes());
                }
            });

            return mappedAuthorities;
        };
    }

    private void loadUserInfo(Set<GrantedAuthority> mappedAuthorities, Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String picture = (String) attributes.get("picture");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email).or(() -> registerUser(email, picture, name)).orElseThrow();

        rolesRepository.findRolesByUser(user.getId()).forEach(role ->
                mappedAuthorities.add(new SimpleGrantedAuthority(role)));
    }

    private Optional<User> registerUser(String email, String picture, String name) {
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setPicture(picture);

        userRepository.save(user);

        Login login = new Login();

        login.setUserId(user.getId());
        login.setEmail(email);
        login.setId(UUID.randomUUID().toString());

        loginRepository.save(login);

        return Optional.of(user);
    }

    public OidcUser loadUser(OidcUserRequest userRequest) {
        Map<String, Object> attributes = new HashMap<>(userRequest.getIdToken().getClaims());
        String email = (String) attributes.get("email");
        String picture = (String) attributes.get("picture");
        String name = (String) attributes.get("name");
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        User user = userRepository.findByEmail(email).or(() -> registerUser(email, picture, name)).orElseThrow();

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
