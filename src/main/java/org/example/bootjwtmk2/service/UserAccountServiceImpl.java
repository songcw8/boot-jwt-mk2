package org.example.bootjwtmk2.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.auth.JwtTokenProvider;
import org.example.bootjwtmk2.mode.dto.UserAccountRequestDTO;
import org.example.bootjwtmk2.mode.entity.UserAccount;
import org.example.bootjwtmk2.mode.dto.TokenResponseDTO;
import org.example.bootjwtmk2.mode.repository.UserAccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountService userAccountService;

    @Override
    public void join(UserAccountRequestDTO dto) throws BadRequestException {
        if (dto.username().isEmpty() || dto.password().isEmpty()) {
            throw new BadRequestException("비어 있는 항목이 있습니다.");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(dto.username());
        userAccount.setPassword(
                passwordEncoder.encode(dto.password()) // 비밀번호를 평문으로 넣으면 안됨.
        );
        userAccount.setRole("USER");
        try{
            userAccountRepository.save(userAccount);
        } catch (DataIntegrityViolationException ex){
            throw new BadRequestException("중복된 Username 입니다.");
        }
    }

    @Override
    public TokenResponseDTO login(UserAccountRequestDTO dto) throws BadRequestException {
        if (dto.username().isEmpty() || dto.password().isEmpty()) {
            throw new BadRequestException("비어 있는 항목이 있습니다.");
        }
        if (userAccountRepository.findByUsername(dto.username()).isEmpty()) {
            throw new UsernameNotFoundException("없는 유저입니다.");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        UserAccount account = userAccountRepository.findByUsername(dto.username()).orElseThrow();
        String token = jwtTokenProvider.generateToken(authentication, List.of(account.getRole()));
        return new TokenResponseDTO(token);
    }
}
