package org.example.bootjwtmk2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.mode.dto.UserAccountJoinDTO;
import org.example.bootjwtmk2.mode.entity.UserAccount;
import org.example.bootjwtmk2.service.UserAccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountService userAccountService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(UserAccountJoinDTO dto) throws BadRequestException {
        // 어떤 예외를 허용할 거냐?
        userAccountService.join(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
