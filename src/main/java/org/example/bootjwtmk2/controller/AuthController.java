package org.example.bootjwtmk2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.example.bootjwtmk2.mode.dto.UserAccountRequestDTO;
import org.example.bootjwtmk2.mode.dto.TokenResponseDTO;
import org.example.bootjwtmk2.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(UserAccountRequestDTO dto) throws BadRequestException {
        TokenResponseDTO tokenResponseDTO = userAccountService.login(dto);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(UserAccountRequestDTO dto) throws BadRequestException {
        // 어떤 예외를 허용할 거냐?
        userAccountService.join(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/join2")
    public ResponseEntity<Void> join2(UserAccountRequestDTO dto) throws BadRequestException {
        // 어떤 예외를 허용할 거냐?
        userAccountService.joinAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
