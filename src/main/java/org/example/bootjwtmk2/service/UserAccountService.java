package org.example.bootjwtmk2.service;

import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.mode.dto.UserAccountJoinDTO;
import org.example.bootjwtmk2.mode.entity.UserAccount;

public interface UserAccountService {
    void join(UserAccountJoinDTO dto) throws BadRequestException;
}
