package com.example.easymoneymapapi.security;

public class MemberRole implements UserRole {
    @Override
    public boolean canAddUser() {
        return false;
    }

    @Override
    public boolean canRemoveUser(UserRole userRole) {
        return false;
    }

    @Override
    public boolean canEditEvent() {
        return false;
    }

    @Override
    public boolean canEditUserRole(UserRole userRole) {
        return false;
    }

    @Override
    public boolean canDeleteEvent() {
        return false;
    }
}
