package com.example.easymoneymapapi.dto;
import lombok.Data;

@Data
public class UserInfoDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public UserInfoDTO() {
    }
    public UserInfoDTO(String username, String firstName, String lastName, String email, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
