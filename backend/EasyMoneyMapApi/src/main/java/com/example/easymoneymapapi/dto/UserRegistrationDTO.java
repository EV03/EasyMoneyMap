package com.example.easymoneymapapi.dto;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import com.example.easymoneymapapi.model.UserInfo;
import lombok.Data;
@Data
public class UserRegistrationDTO {

    // min und max validiert numerisch deswegen für alle strings ändernn

    //@NotBlank(message = "Username muss angegeben werden")
    //@Size(min = 4, max = 20, message = "Username muss zwischen 4 und 20 Zeichen groß sein")
    private String username;

    //@NotBlank(message = "Es muss ein Passwort angegeben werden")
    // @Size(min = 8, max = 20, message = "Passwort muss zwischen 8 und 20 Zeichen groß sein")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$", message = "Passwort muss mindestens eine Zahl und einen Buchstaben enthalten")
    private String password;

   // @Email(message = "Ungültige E-Mail angegeben")
   // @NotBlank(message = "E-Mail muss angegeben werden")
    private String email;

   // @NotBlank(message = "Vorname muss angegeben werden")
   // @Size(max = 20, message = "Vorname darf nur bis zu 20 Zeichen haben")
    private String firstName;

    //@NotBlank(message = "Nachname muss angegeben werden")
    //@Size(max = 20, message = "Nachname darf nur bis zu 20 Zeichen haben")
    private String lastName;


    public UserInfo mapToUserInfo( ) {

        UserInfo user = new UserInfo();
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setEmail(this.getEmail());
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        return user;

    }
}
