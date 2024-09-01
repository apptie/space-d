package com.dnd.spaced.global.auth.security.core;

import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.global.auth.exception.BlockedTokenException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class OAuth2UserDetailsService implements UserDetailsService {

    private final TokenDecoder tokenDecoder;
    private final BlacklistTokenService blacklistTokenService;

    @Override
    public OAuth2UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return tokenDecoder.decode(TokenType.ACCESS, token)
                           .map(this::convert)
                           .orElse(null);
    }

    private OAuth2UserDetails convert(PrivateClaims privateClaims) {
        validatePrivateClaims(privateClaims);

        return new OAuth2UserDetails(
                privateClaims.id(),
                Set.of(new SimpleGrantedAuthority(privateClaims.roleName()))
        );
    }

    private void validatePrivateClaims(PrivateClaims privateClaims) {
        if (blacklistTokenService.isBlockedToken(privateClaims)) {
            throw new BlockedTokenException();
        }
    }
}
