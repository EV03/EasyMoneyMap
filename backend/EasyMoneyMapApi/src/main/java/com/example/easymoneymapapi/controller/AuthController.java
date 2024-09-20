package com.example.easymoneymapapi.controller;

        import com.example.easymoneymapapi.dto.AuthRequest;
        import com.example.easymoneymapapi.dto.JwtResponse;
        import com.example.easymoneymapapi.dto.UserRegistrationDTO;
        import com.example.easymoneymapapi.model.UserInfo;
        import com.example.easymoneymapapi.service.AuthService;
        import com.example.easymoneymapapi.service.JwtService;
        import com.example.easymoneymapapi.service.UserService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import com.example.easymoneymapapi.exception.ValidationException;

        import java.util.HashMap;
        import java.util.Map;


/**
 * AuthController verwaltet die Authentifizierungs- und Registrierungsanfragen.
 *
 * Alle Fehler werden global durch den GlobalExceptionHandler behandelt.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    /**
     * Registriert einen neuen Benutzer. Validierungsfehler und andere Ausnahmen
     * werden global behandelt, siehe GlobalExceptionHandler.
     *
     * @param UserRegistrationDTO die zu registrierenden Benutzerdaten
     * @return eine Bestätigungsmeldung
     */

    @PostMapping("/register")
    public ResponseEntity<String> register( @RequestBody UserRegistrationDTO userReq) {

        Map<String, String> errors = new HashMap<>();
        // Manuelle Validierung (Spring validation läuft nicht)
        if (userReq.getUsername() == null || userReq.getUsername().isBlank()) {
            errors.put("username", "Username muss angegeben werden");
        } else if (userReq.getUsername().length() < 4 || userReq.getUsername().length() > 20) {
            errors.put("username", "Username muss zwischen 4 und 20 Zeichen groß sein");
        }

        if (userReq.getPassword() == null || userReq.getPassword().isBlank()) {
            errors.put("password", "Es muss ein Passwort angegeben werden");
        } else if (userReq.getPassword().length() < 8 || userReq.getPassword().length() > 20) {
            errors.put("password", "Passwort muss zwischen 8 und 20 Zeichen groß sein");
        } else if (!userReq.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$")) {
            errors.put("password", "Passwort muss mindestens eine Zahl und einen Buchstaben enthalten");
        }

        if (userReq.getEmail() == null || userReq.getEmail().isBlank()) {
            errors.put("email", "E-Mail muss angegeben werden");
        } else if (!userReq.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.put("email", "Ungültige E-Mail angegeben");
        }

        if (userReq.getFirstName() == null || userReq.getFirstName().isBlank()) {
            errors.put("firstName", "Vorname muss angegeben werden");
        } else if (userReq.getFirstName().length() > 20) {
            errors.put("firstName", "Vorname darf nur bis zu 20 Zeichen enthalten");
        }

        if (userReq.getLastName() == null || userReq.getLastName().isBlank()) {
            errors.put("lastName", "Nachname muss angegeben werden");
        } else if (userReq.getLastName().length() > 20) {
            errors.put("lastName", "Nachname darf nur bis zu 20 Zeichen enthalten");
        }

        // Wenn Fehler aufgetreten sind, werfe die ValidationException
        if (!errors.isEmpty()) {
            throw new ValidationException("Validierungsfehler",errors);
        }

        UserInfo user = userReq.mapToUserInfo();
        userService.registerUser(user);
        return ResponseEntity.ok("Benutzer erfolgreich registriert");
    }

    /**
     * Authentifiziert einen Benutzer und gibt ein JWT-Token zurück. Bei ungültigen
     * Anmeldedaten wird eine BadCredentialsException geworfen, die von Spring Security behandelt wird.
     *
     * @param authRequest die Anmeldedaten
     * @return ein JWT-Token bei erfolgreicher Authentifizierung
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

            String token = authService.authenticateAndGenerateToken(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
    }
}
