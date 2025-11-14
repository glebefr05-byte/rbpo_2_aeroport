package com.example.rbpo_aeroport.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.rbpo_aeroport.entities.ApplicationUser;
import com.example.rbpo_aeroport.repositories.ApplicationUserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApplicationUserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return fromApplicationUser(user);
    }

    public UserDetails fromApplicationUser(ApplicationUser user) {
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getGrantedAuthorities())
                .accountLocked(user.isAccountLocked())
                .accountExpired(user.isAccountExpired())
                .credentialsExpired(user.isCredentialsExpired())
                .disabled(user.isDisabled())
                .build();
    }
}