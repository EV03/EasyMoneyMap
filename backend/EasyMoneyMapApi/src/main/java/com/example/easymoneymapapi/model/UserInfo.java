package com.example.easymoneymapapi.model;
import com.example.easymoneymapapi.dto.UserInfoDTO;
import com.example.easymoneymapapi.dto.UserRegistrationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    public UserInfo(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @OneToMany(mappedBy = "user")
    private Set<UserEvent> userEvents = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserKauf> userPurchases = new HashSet<>();
}
