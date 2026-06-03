package com.ecommerce.service;

import com.ecommerce.entity.SysUser;
import com.ecommerce.repository.SysUserRepository;
import com.ecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final MenuService menuService;

    public LoginResult login(String username, String password) {
        Optional<SysUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        SysUser user = userOptional.get();
        if (user.getStatus() != null && user.getStatus() != 1) {
            throw new RuntimeException("账号已禁用");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleCode());
        String tokenId = jwtUtil.getTokenIdFromToken(token);
        Date expiresAt = jwtUtil.getExpirationDateFromToken(token);
        authTokenService.storeToken(tokenId, user.getId(), user.getUsername(), user.getRoleCode(), expiresAt);
        Set<String> permissions = menuService.getPermissions(user.getRoleCode());
        return new LoginResult(token, expiresAt.getTime(), user, permissions);
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        authTokenService.revokeToken(jwtUtil.getTokenIdFromToken(token));
    }

    public SysUser getCurrentUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public SysUser getCurrentUserByToken(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return getCurrentUser(userId);
    }

    public SysUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public record LoginResult(String token, long expiresAt, SysUser user, Set<String> permissions) {
    }
}
