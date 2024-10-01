package com.example.easymoneymapapi.security;

import jakarta.persistence.Embeddable;

/**
 * Repräsentiert eine Rolle, die ein Benutzer in einem Event haben kann
 * name der Rolle wird einfach als String in der Datenbank gespeichert
 * Logik der Rolle wird über die Registry geholt
 */

@Embeddable
public class Role {
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public UserRole getRoleLogic() {
        return UserRoleRegistry.getRole(name);
    }
}
