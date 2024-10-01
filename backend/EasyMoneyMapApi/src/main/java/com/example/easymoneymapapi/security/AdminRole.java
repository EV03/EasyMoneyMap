package com.example.easymoneymapapi.security;

public class AdminRole implements UserRole {
    @Override
    public boolean canAddUser() {
        return true;
    }

    @Override
    public boolean canRemoveUser(UserRole userRole) {
        return (userRole instanceof MemberRole);
    }

    @Override
    public boolean canEditEvent() {
        return true;
    }

    @Override
    public boolean canEditUserRole(UserRole userRole) {
        return userRole instanceof MemberRole;
    }

    @Override
    public boolean canDeleteEvent() {
        return false;
    }
}
