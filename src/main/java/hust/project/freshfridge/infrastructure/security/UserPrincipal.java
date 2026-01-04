package hust.project.freshfridge.infrastructure.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import hust.project.freshfridge.domain.entity.User;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final String role;
    private final Boolean isVerified;
    private final Boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getIsVerified(),
                user.getIsActive(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive != null && isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive != null && isActive;
    }
}
