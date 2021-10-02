package org.uresti.pozarreal.testmode.config;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.uresti.pozarreal.testmode.service.TestSystemUserDetailsService;

@Component
@Profile("tests")
public class TestAuthenticationProvider implements AuthenticationProvider {

    private final TestSystemUserDetailsService userDetailsService;

    public TestAuthenticationProvider(TestSystemUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userDetailsService.loadUser(name, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
