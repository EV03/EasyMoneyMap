package com.example.easymoneymapapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Size(min = 4, max = 20)
    private String username;

    @Size(min = 8, max = 20)
    private String password;
}
