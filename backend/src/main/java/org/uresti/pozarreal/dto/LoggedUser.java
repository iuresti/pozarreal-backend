package org.uresti.pozarreal.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

@Builder
@Value
public class LoggedUser implements OidcUser {

    OidcIdToken oidcIdToken;
    OidcUserInfo oidcUserInfo;
    Collection<? extends GrantedAuthority> grantedAuthorities;
    String name;
    Map<String, Object> claims;

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUserInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcIdToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUserId() {
        return getClaimAsString("userId");
    }
}
