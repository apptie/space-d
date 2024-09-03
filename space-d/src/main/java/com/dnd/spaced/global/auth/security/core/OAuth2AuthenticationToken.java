package com.dnd.spaced.global.auth.security.core;

import com.dnd.spaced.global.auth.exception.UnsupportedSecurityOperationException;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OAuth2AuthenticationToken extends AbstractAuthenticationToken {

    private final OAuth2UserDetails principal;

    public OAuth2AuthenticationToken(OAuth2UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public OAuth2UserDetails getPrincipal() {
        return principal;
    }
}
