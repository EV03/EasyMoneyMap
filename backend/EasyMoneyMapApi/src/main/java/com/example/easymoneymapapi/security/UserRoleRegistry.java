package com.example.easymoneymapapi.security;

import java.util.HashMap;
import java.util.Map;

public class UserRoleRegistry {
    private static final Map<String,UserRole> roles = new HashMap<>();

    static {
        roles.put("CREATOR", new CreatorRole());
        roles.put("ADMIN", new AdminRole());
        roles.put("MEMBER", new MemberRole());
        // hier können weitere Rollen hinzugefügt werden
    }
    public static UserRole getRole(String roleName) {
        UserRole role = roles.get(roleName.toUpperCase());
        if (role == null) {
            throw new IllegalArgumentException("Unknown role: " + roleName);
        }
        return role;
    }

    public static void registerRole(String roleName, UserRole userRole) {
        roles.put(roleName.toUpperCase(), userRole);
    }
}
