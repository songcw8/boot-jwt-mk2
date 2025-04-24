package org.example.bootjwtmk2.service;

import lombok.RequiredArgsConstructor;
import org.example.bootjwtmk2.mode.entity.UserAccount;
import org.example.bootjwtmk2.mode.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 읎다고요. %s".formatted(username)));

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .build();
    }
}
