package org.example.bootjwtmk2.mode.dto;

// 이 DTO를 만드는 이유? 일반적으로 응답은 죄다 JSON이라서
public record TokenResponseDTO(String accessToken) {
}
