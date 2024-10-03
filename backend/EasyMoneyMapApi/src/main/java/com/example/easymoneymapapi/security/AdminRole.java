package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.model.UserEvent;

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

    @Override
    public void editRole(UserEvent userEvent, UserRole currentUserRole) {
        if(currentUserRole instanceof MemberRole) {
            userEvent.setRole(new Role("admin"));
        }
    }
}
