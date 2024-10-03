package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.model.UserEvent;

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


    // sollte nicht aufgerufen werden können, da member keine Rollen ändern kann
    @Override
    public void editRole(UserEvent userEvent, UserRole currentUserRole) {
        return;
    }
}
