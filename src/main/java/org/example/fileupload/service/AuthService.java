package org.example.fileupload.service;

import lombok.RequiredArgsConstructor;
import org.example.fileupload.dto.LoginRequest;
import org.example.fileupload.dto.LoginResponse;
import org.example.fileupload.entity.User;
import org.example.fileupload.repo.UserRepository;
import org.example.fileupload.securyti.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        // 1. Email bo'yicha user topamiz
        User user = userRepository.findByEmail(request.getEmail())   // ← requestda email bo'lishi kerak
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Email topilmadi: " + request.getEmail()));

        // 2. Parolni tekshiramiz
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Parol noto'g'ri");
        }

        // 3. Hisob holatini tekshirish (ixtiyoriy, lekin yaxshi amaliyot)
        if (!user.isEnabled()) {
            throw new BadCredentialsException("Hisob faol emas");
        }
        if (!user.isAccountNonLocked()) {
            throw new BadCredentialsException("Hisob bloklangan");
        }

        // 4. JWT token yaratamiz
        String jwt = jwtUtil.generateToken(user);

        // 5. Javob qaytaramiz
        LoginResponse response = LoginResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .username(user.getEmail())               // yoki user.getFullName() bo'lishi mumkin
                .authorities(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();

        return response;
    }
}