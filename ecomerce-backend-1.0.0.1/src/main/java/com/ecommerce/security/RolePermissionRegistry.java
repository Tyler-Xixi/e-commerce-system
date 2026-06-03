package com.ecommerce.security;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class RolePermissionRegistry {
    private static final Set<String> ALL_PERMISSIONS = Set.of(
            "dashboard:todo",
            "dashboard:approval",
            "sourcing:demand:list",
            "sourcing:demand:create",
            "sourcing:demand:review",
            "sourcing:rfq:list",
            "sourcing:rfq:manage",
            "sourcing:quote:summary",
            "sourcing:quote:create",
            "sourcing:award:list",
            "sourcing:award:manage",
            "sourcing:award:approve",
            "supplier:list",
            "supplier:manage",
            "product:list",
            "product:manage",
            "purchase:order:list",
            "purchase:order:manage",
            "purchase:payment:list",
            "purchase:payment:manage",
            "warehouse:inventory",
            "warehouse:inventory:manage",
            "warehouse:logistics",
            "warehouse:logistics:manage",
            "aftersales:list",
            "aftersales:manage",
            "system:user:manage",
            "system:role:manage",
            "system:menu:manage"
    );

    private static final Map<String, Set<String>> ROLE_PERMISSIONS = Map.of(
            "JS01", Set.of(
                    "dashboard:todo",
                    "dashboard:approval",
                    "sourcing:demand:list",
                    "sourcing:demand:create",
                    "sourcing:rfq:list",
                    "sourcing:rfq:manage",
                    "sourcing:quote:summary",
                    "sourcing:award:list",
                    "sourcing:award:manage",
                    "product:list",
                    "product:manage",
                    "warehouse:inventory"
            ),
            "JS02", Set.of(
                    "dashboard:todo",
                    "dashboard:approval",
                    "sourcing:demand:list",
                    "sourcing:demand:review",
                    "sourcing:rfq:list",
                    "sourcing:rfq:manage",
                    "sourcing:quote:summary",
                    "product:list",
                    "warehouse:inventory"
            ),
            "JS03", ALL_PERMISSIONS,
            "JS04", ALL_PERMISSIONS,
            "JS05", Set.of(
                    "dashboard:todo",
                    "sourcing:rfq:list",
                    "sourcing:quote:summary",
                    "sourcing:quote:create",
                    "sourcing:award:list"
            ),
            "JS06", Set.of(
                    "dashboard:todo",
                    "warehouse:inventory",
                    "warehouse:inventory:manage",
                    "warehouse:logistics",
                    "warehouse:logistics:manage"
            )
    );

    private RolePermissionRegistry() {
    }

    public static Set<String> defaultPermissions(String roleCode) {
        return new LinkedHashSet<>(ROLE_PERMISSIONS.getOrDefault(roleCode, Set.of("dashboard:todo")));
    }

    public static Set<String> allPermissions() {
        return new LinkedHashSet<>(ALL_PERMISSIONS);
    }
}
