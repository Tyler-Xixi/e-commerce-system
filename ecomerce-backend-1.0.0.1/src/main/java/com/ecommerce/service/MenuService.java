package com.ecommerce.service;

import com.ecommerce.entity.SysMenu;
import com.ecommerce.entity.SysRoleMenu;
import com.ecommerce.repository.SysMenuRepository;
import com.ecommerce.repository.SysRoleMenuRepository;
import com.ecommerce.security.RolePermissionRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private static final String PERMISSION_CACHE_PREFIX = "ecommerce:permission:";
    private static final Duration PERMISSION_CACHE_TTL = Duration.ofMinutes(30);

    private final SysMenuRepository menuRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public List<Map<String, Object>> getMenuTree(String roleCode) {
        List<SysRoleMenu> roleMenus = roleMenuRepository.findByRoleCode(roleCode);
        Set<Long> menuIds = roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toSet());

        List<SysMenu> allMenus = menuRepository.findAll();
        List<SysMenu> authorizedMenus = allMenus.stream()
                .filter(menu -> menuIds.contains(menu.getId()))
                .filter(menu -> menu.getVisible() == 1)
                .collect(Collectors.toList());

        return buildMenuTree(authorizedMenus, 0L);
    }

    private List<Map<String, Object>> buildMenuTree(List<SysMenu> menus, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        List<SysMenu> childMenus = menus.stream()
                .filter(m -> Objects.equals(m.getParentId(), parentId))
                .sorted(Comparator.comparingInt(m -> m.getSortOrder() != null ? m.getSortOrder() : 0))
                .collect(Collectors.toList());

        for (SysMenu menu : childMenus) {
            Map<String, Object> menuNode = new HashMap<>();
            menuNode.put("id", menu.getId());
            menuNode.put("menuName", menu.getMenuName());
            menuNode.put("path", menu.getPath());
            menuNode.put("component", menu.getComponent());
            menuNode.put("icon", menu.getIcon());
            menuNode.put("permission", menu.getPermission());

            List<Map<String, Object>> children = buildMenuTree(menus, menu.getId());
            if (!children.isEmpty()) {
                menuNode.put("children", children);
            }

            tree.add(menuNode);
        }

        return tree;
    }

    public Set<String> getPermissions(String roleCode) {
        String normalizedRoleCode = roleCode == null || roleCode.isBlank() ? "JS01" : roleCode;
        Set<String> cached = readPermissionCache(normalizedRoleCode);
        if (cached != null) {
            return cached;
        }

        Set<String> permissions = RolePermissionRegistry.defaultPermissions(normalizedRoleCode);
        try {
            List<SysRoleMenu> roleMenus = roleMenuRepository.findByRoleCode(normalizedRoleCode);
            List<Long> menuIds = roleMenus.stream()
                    .map(SysRoleMenu::getMenuId)
                    .collect(Collectors.toList());

            permissions.addAll(menuRepository.findAllById(menuIds).stream()
                    .map(SysMenu::getPermission)
                    .filter(p -> p != null && !p.isEmpty())
                    .collect(Collectors.toSet()));
        } catch (Exception e) {
            logger.warn("Permission table read failed, using built-in role permissions: {}", e.getMessage());
        }

        writePermissionCache(normalizedRoleCode, permissions);
        return permissions;
    }

    private Set<String> readPermissionCache(String roleCode) {
        try {
            String value = redisTemplate.opsForValue().get(PERMISSION_CACHE_PREFIX + roleCode);
            if (value == null || value.isBlank()) {
                return null;
            }
            return objectMapper.readValue(value, new TypeReference<LinkedHashSet<String>>() {});
        } catch (Exception e) {
            logger.warn("Redis permission cache read failed: {}", e.getMessage());
            return null;
        }
    }

    private void writePermissionCache(String roleCode, Set<String> permissions) {
        try {
            redisTemplate.opsForValue().set(
                    PERMISSION_CACHE_PREFIX + roleCode,
                    objectMapper.writeValueAsString(permissions),
                    PERMISSION_CACHE_TTL
            );
        } catch (Exception e) {
            logger.warn("Redis permission cache write failed: {}", e.getMessage());
        }
    }
}
