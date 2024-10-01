package com.example.easymoneymapapi.model;

import com.example.easymoneymapapi.security.Role;
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

    @Embedded
    private Role role;
}
