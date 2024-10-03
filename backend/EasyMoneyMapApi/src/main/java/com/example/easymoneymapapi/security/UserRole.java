package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.model.UserEvent;

public interface UserRole {

    boolean canAddUser();
    boolean canRemoveUser(UserRole userRole);
    boolean canEditEvent();
    boolean canEditUserRole(UserRole userRole);
    boolean canDeleteEvent();

    void editRole(UserEvent userEvent, UserRole currentUserRole);
}
