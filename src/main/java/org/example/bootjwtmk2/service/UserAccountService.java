package org.example.bootjwtmk2.service;

import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.mode.dto.UserAccountRequestDTO;
import org.example.bootjwtmk2.mode.dto.TokenResponseDTO;

public interface UserAccountService {
    void join(UserAccountRequestDTO dto) throws BadRequestException;

    TokenResponseDTO login(UserAccountRequestDTO dto) throws BadRequestException;

    void joinAdmin(UserAccountRequestDTO dto) throws BadRequestException;

}
