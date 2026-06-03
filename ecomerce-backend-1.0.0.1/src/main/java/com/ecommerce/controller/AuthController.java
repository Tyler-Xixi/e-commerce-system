package com.ecommerce.controller;

import com.ecommerce.entity.SysUser;
import com.ecommerce.security.JwtPrincipal;
import com.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名和密码不能为空"));
        }
        try {
            AuthService.LoginResult loginResult = authService.login(username, password);
            SysUser user = loginResult.user();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("token", loginResult.token());
            result.put("expiresAt", loginResult.expiresAt());
            result.put("username", user.getUsername());
            result.put("userId", user.getId());
            result.put("roleCode", user.getRoleCode());
            result.put("roleName", getRoleName(user.getRoleCode()));
            result.put("permissions", loginResult.permissions());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(extractBearerToken(request));
        return ResponseEntity.ok(Map.of("message", "退出成功"));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtPrincipal principal)) {
            return ResponseEntity.status(401).body(Map.of("message", "未认证"));
        }
        SysUser user = authService.getCurrentUser(principal.getUserId());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("message", "用户不存在"));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("name", user.getRealName());
        result.put("roleCode", user.getRoleCode());
        result.put("role", getRoleName(user.getRoleCode()));
        result.put("roleName", getRoleName(user.getRoleCode()));
        result.put("dept", "运营部");
        result.put("status", Integer.valueOf(1).equals(user.getStatus()) ? "启用" : "禁用");
        result.put("permissions", principal.getAuthorities().stream()
                .map(Object::toString)
                .filter(authority -> !authority.startsWith("ROLE_"))
                .toList());
        return ResponseEntity.ok(result);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private String getRoleName(String code) {
        return switch (code) {
            case "JS01" -> "品类工程师";
            case "JS02" -> "品类组长";
            case "JS03" -> "采购处长";
            case "JS04" -> "平台管理员";
            case "JS05" -> "供应商操作员";
            case "JS06" -> "仓储调度员";
            default -> "未知";
        };
    }
}
