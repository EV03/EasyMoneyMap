package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.model.UserEvent;

public class CreatorRole implements UserRole {
    @Override
    public boolean canAddUser() {
        return true; // Creator kann Benutzer hinzufügen
    }

    @Override
    public boolean canRemoveUser(UserRole userRole) {
        return !(userRole instanceof CreatorRole); // Creator kann sich selbst nicht entfernen
    }

    @Override
    public boolean canEditEvent() {
        return true; // Creator kann das Event bearbeiten
    }

    @Override
    public boolean canEditUserRole(UserRole userRole) {
        return true; // Creator kann die Rollen aller Benutzer bearbeiten
    }

    @Override
    public boolean canDeleteEvent() {
        return true; // Creator kann das Event löschen
    }

    @Override
    public void editRole(UserEvent userEvent, UserRole userRole) {
        if (userRole instanceof AdminRole) {
            userEvent.setRole(new Role("member"));
        }
        else if (userRole instanceof MemberRole) {
            userEvent.setRole(new Role("admin"));
        }
    }
}
