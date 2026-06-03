package com.ecommerce.security.filter;

import com.ecommerce.security.JwtPrincipal;
import com.ecommerce.service.AuthTokenService;
import com.ecommerce.service.MenuService;
import com.ecommerce.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final AuthTokenService authTokenService;
    private final MenuService menuService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || "/auth/login".equals(path)
                || path.startsWith("/public/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(jwtUtil.getHeader());

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseToken(token);
                String username = claims.getSubject();
                String tokenId = claims.getId();
                String roleCode = claims.get("roleCode", String.class);
                Long userId = toLong(claims.get("userId"));

                if (!authTokenService.isTokenActive(tokenId)) {
                    sendUnauthorized(response, "Token has expired or has been revoked");
                    return;
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Set<String> permissions = menuService.getPermissions(roleCode);
                    JwtPrincipal principal = new JwtPrincipal(userId, username, roleCode, permissions);
                    var authentication = new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.warn("JWT authentication failed for {}: {}", request.getRequestURI(), e.getMessage());
                SecurityContextHolder.clearContext();
                sendUnauthorized(response, "Invalid or expired token");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }

    private Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value != null) {
            return Long.valueOf(value.toString());
        }
        return null;
    }
}
