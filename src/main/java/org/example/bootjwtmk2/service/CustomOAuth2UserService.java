package org.example.bootjwtmk2.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bootjwtmk2.auth.JwtTokenProvider;
import org.example.bootjwtmk2.mode.entity.KakaoUser;
import org.example.bootjwtmk2.mode.repository.KakaoUserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final KakaoUserRepository kakaoUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

        String kakaoId = attributes.get("kakao_id").toString();
        String email = attributes.get("email").toString();
        String nickname = profile.get("nickname").toString();
        String username = "kakao_%s".formatted(kakaoId);

        KakaoUser kakaoUser = kakaoUserRepository.findByUsername(username)
                .orElseGet(() -> {
                    KakaoUser newUser = new KakaoUser();
                    newUser.setUsername(username);
                    //newUser.setEmail(email);
                    newUser.setName(nickname);
                    return kakaoUserRepository.save(newUser);
                });

        return oAuth2User;
    }

    @Service
    @RequiredArgsConstructor
    public static class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        private final JwtTokenProvider jwtTokenProvider;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
            String id = oAuth2User.getAttributes().get("id").toString();
            String username = "kakao_%s".formatted(id);
            String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(username, ""), List.of("KAKAO"));

            response.setContentType("application/json");
            response.getWriter().write(
                    """
                            {"token" : "%s"}
                            """.formatted(token));
        }
    }
}
