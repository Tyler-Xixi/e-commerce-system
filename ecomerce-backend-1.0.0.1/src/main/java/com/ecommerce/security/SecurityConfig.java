package com.ecommerce.security;

import com.ecommerce.security.filter.JwtAuthenticationFilter;
import com.ecommerce.security.handler.AccessDeniedHandlerImpl;
import com.ecommerce.security.handler.AuthenticationEntryPointImpl;
import com.ecommerce.service.AuthTokenService;
import com.ecommerce.service.MenuService;
import com.ecommerce.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Arrays;

/**
 * Spring Security 安全配置类
 * 
 * 配置项目的安全策略，包括：
 * - JWT认证过滤器
 * - 密码加密方式
 * - CORS跨域配置
 * - 请求权限控制
 * - 异常处理
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 配置JWT认证过滤器Bean
     * 
     * @param jwtUtil JWT工具类，用于解析和验证Token
     * @param authTokenService Token管理服务，用于验证Token有效性
     * @param menuService 菜单服务，用于获取用户权限
     * @return JwtAuthenticationFilter实例
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, AuthTokenService authTokenService, MenuService menuService) {
        return new JwtAuthenticationFilter(jwtUtil, authTokenService, menuService);
    }

    /**
     * 配置密码加密器Bean
     * 
     * 使用BCryptPasswordEncoder进行密码加密，安全性高，支持自动加盐
     * 
     * @return PasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置CORS跨域资源共享
     * 
     * 允许前端跨域访问后端API，配置允许的来源、方法、请求头
     * 
     * @return CorsConfigurationSource实例
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有来源（生产环境应限制具体域名）
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 允许的HTTP方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允许携带凭证（如Cookie、Token）
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 配置安全过滤链
     * 
     * 核心安全配置方法，定义了认证和授权策略：
     * - 启用CORS
     * - 禁用CSRF（使用JWT不需要CSRF）
     * - 无状态会话管理（JWT不需要Session）
     * - 禁用HTTP Basic和表单登录
     * - 配置请求权限规则
     * - 配置异常处理器
     * - 添加JWT过滤器
     * 
     * @param http HttpSecurity构建器
     * @param jwtFilter JWT认证过滤器
     * @return Security ArFilterChain实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 禁用CSRF（JWT方式不需要CSRF保护）
            .csrf(csrf -> csrf.disable())
            // 配置会话管理为无状态（前后端分离架构）
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 禁用HTTP Basic认证
            .httpBasic(httpBasic -> httpBasic.disable())
            // 禁用表单登录（使用JSON API登录）
            .formLogin(form -> form.disable())
            // 不需要显式保存SecurityContext
            .securityContext(securityContext -> securityContext.requireExplicitSave(false))
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // OPTIONS请求直接放行（预检请求）
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // 登录接口和公共接口放行
                .requestMatchers("/auth/login", "/public/**").permitAll()
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // 配置异常处理
            .exceptionHandling(exception -> exception
                // 处理权限不足异常
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                // 处理未认证异常
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
            )
            // 在UsernamePasswordAuthenticationFilter之前添加JWT过滤器
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}