package be.ddd.infra.security;

import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class Auth0UserPrincipal implements UserDetails {

    private final Long memberId;
    private final String auth0Sub;

    public Auth0UserPrincipal(Long memberId, String auth0Sub) {
        this.memberId = memberId;
        this.auth0Sub = auth0Sub;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null; // Passwords are not stored in the principal for JWT authentication
    }

    @Override
    public String getUsername() {
        return auth0Sub; // Auth0 subject is the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
