package com.example.easymoneymapapi.model;

import com.example.easymoneymapapi.dto.UserInfoDTO;
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

    public UserInfoDTO mapToUserInfoDTO() {
        return new UserInfoDTO(this.getUser().getUsername(),this.getUser().getFirstName(),
                this.getUser().getLastName(), this.getUser().getEmail(), this.getRole().getName());
    }
}
