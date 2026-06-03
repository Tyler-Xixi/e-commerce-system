package com.ecommerce.controller;

import com.ecommerce.security.JwtPrincipal;
import com.ecommerce.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/tree")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMenuTree(Authentication authentication) {
        String roleCode = getRoleCode(authentication);
        List<Map<String, Object>> menuTree = menuService.getMenuTree(roleCode);
        return ResponseEntity.ok(menuTree);
    }

    @GetMapping("/permissions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPermissions(Authentication authentication) {
        String roleCode = getRoleCode(authentication);
        Set<String> permissions = menuService.getPermissions(roleCode);
        return ResponseEntity.ok(permissions);
    }

    private String getRoleCode(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof JwtPrincipal principal) {
            return principal.getRoleCode();
        }
        return null;
    }
}
