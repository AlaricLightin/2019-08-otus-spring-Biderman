package ru.biderman.librarywebclassic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final GrantedAuthority USER = new SimpleGrantedAuthority("ROLE_USER");

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .map(user -> new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return user.isAdmin() ?
                                Arrays.asList(ADMIN, USER) : Collections.singletonList(USER);
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getUsername();
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
                })
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' not found", s)));
    }
}
