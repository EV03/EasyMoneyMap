package com.example.easymoneymapapi.security;

public interface UserRole {

    boolean canAddUser();
    boolean canRemoveUser(UserRole userRole);
    boolean canEditEvent();
    boolean canEditUserRole(UserRole userRole);
    boolean canDeleteEvent();
}
