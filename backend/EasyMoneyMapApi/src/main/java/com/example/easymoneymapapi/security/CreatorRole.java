package com.example.easymoneymapapi.security;

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
}
