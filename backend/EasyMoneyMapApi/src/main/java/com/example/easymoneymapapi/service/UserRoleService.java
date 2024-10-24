package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.security.UserRole;
import com.example.easymoneymapapi.security.UserRoleRegistry;

/**
 * Service zum ersten Anlegen von Rollen
 * und zum Abrufen von Rollen
 */
public class UserRoleService {

    public static UserRole getUserRole(String rolename) {
        return UserRoleRegistry.getRole(rolename);
    }

    // fügt eine neue Rolle hinzu
    public void registerNewRole(String roleName, UserRole userRole) {
        UserRoleRegistry.registerRole(roleName, userRole);
    }

}
