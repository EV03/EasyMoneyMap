package com.example.easymoneymapapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    public enum Role {
        Creator {
            @Override
            public boolean canAddUser() {
                return true; // Creator can add/remove any user
            }

            @Override
            public boolean canRemoveUser(UserEvent.Role userRole) {
                return userRole != Creator; // Creator can't remove themselves
            }
            @Override
            public boolean canEditEvent() {
                return true;
            }

            @Override
            public boolean canEditUserRole(UserEvent.Role userRole) {
                return true; // Creator can edit any user
            }

            public boolean canDeleteEvent() {
                return true; // Members can't delete the event
            }
        },
        Admin {
            @Override
            public boolean canAddUser () {
                return true;// Admins can add any user
            }

            @Override
            public boolean canRemoveUser(UserEvent.Role userRole) {
                    return userRole != Creator && userRole != Admin; // Admins can't remove Creators or other Admins
            }
            @Override
            public boolean canEditEvent() {
                return true;
            }

            @Override
            public boolean canEditUserRole(UserEvent.Role userRole) {
                return userRole == Member; // Admins can only edit Members
            }

            public boolean canDeleteEvent() {
                return false; // Members can't delete the event
            }
        },
        Member {
            @Override
            public boolean canAddUser() {
                return false; // Members can't add/remove users
            }

            @Override
            public boolean canRemoveUser(Role userRole) {
                return false;
            }

            @Override
            public boolean canEditEvent() {
                return false; // Members can't edit the event
            }

            @Override
            public boolean canEditUserRole(UserEvent.Role userRole) {
                return false; // Members can't edit any user
            }

            public boolean canDeleteEvent() {
                return false; // Members can't delete the event
            }
        };

        public abstract boolean canAddUser();
        public abstract boolean canRemoveUser(UserEvent.Role userRole);
        public abstract boolean canEditEvent();
        public abstract boolean canEditUserRole(UserEvent.Role userRole);
        public abstract boolean canDeleteEvent();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private double totalSpent;

    @Enumerated(EnumType.STRING)
    private Role role;
}
