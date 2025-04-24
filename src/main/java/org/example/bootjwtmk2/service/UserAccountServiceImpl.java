package org.example.bootjwtmk2.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.mode.dto.UserAccountJoinDTO;
import org.example.bootjwtmk2.mode.entity.UserAccount;
import org.example.bootjwtmk2.mode.repository.UserAccountRepository;
import org.hibernate.property.access.internal.PropertyAccessCompositeUserTypeImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(UserAccountJoinDTO dto) throws BadRequestException {
        if (dto.username().isEmpty() || dto.password().isEmpty()) {
            throw new BadRequestException("비어 있는 항목이 있습니다.");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(dto.username());
        userAccount.setPassword(
                passwordEncoder.encode(dto.password()) // 비밀번호를 평문으로 넣으면 안됨.
        );
        try{
            userAccountRepository.save(userAccount);
        } catch (DataIntegrityViolationException ex){
            throw new BadRequestException("중복된 Username 입니다.");
        }
    }
}
