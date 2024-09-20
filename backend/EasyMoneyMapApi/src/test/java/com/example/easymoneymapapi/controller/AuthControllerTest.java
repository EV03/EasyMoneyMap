package com.example.easymoneymapapi.controller;


import com.example.easymoneymapapi.model.UserInfo;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * AuthControllerTest testet die Authentifizierungs- und Registrierungsanfragen.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc(addFilters = false) // Deaktiviert Security-Filter für Tests
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void registerUser_success() throws Exception {
        UserInfo user = new UserInfo();
        user.setUsername("validuser");
        user.setEmail("validuser@example.com");
        user.setFirstName("Valid");
        user.setLastName("User");
        user.setPassword("ValidPassword1");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"validuser\",\"password\":\"ValidPassword1\",\"email\":\"validuser@example.com\",\"firstName\":\"Valid\",\"lastName\":\"User\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Benutzer erfolgreich registriert"));
    }

    @Test
    void authenticateAndGetToken_success() throws Exception {

        // nur auf selben Header testen da der rest für jeden token neu generiert wird
        String expectedTokenHeader = "eyJhbGciOiJIUzI1NiJ9";

        UserInfo user = new UserInfo();
        user.setUsername("validuser");
        user.setEmail("validuser@example.com");
        user.setFirstName("Valid");
        user.setLastName("User");
        user.setPassword("ValidPassword1");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"validuser\",\"password\":\"ValidPassword1\",\"email\":\"validuser@example.com\",\"firstName\":\"Valid\",\"lastName\":\"User\"}"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"validuser\",\"password\":\"ValidPassword1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String token = JsonPath.read(jsonResponse, "$.token");
        String tokenHeader = token.split("\\.")[0];

        assertThat(tokenHeader).isEqualTo(expectedTokenHeader);
    }

    @Test
    void authenticateAndGetToken_invalidCredentialsWithWrongUsernameOrPasswort() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"invaliduser\",\"password\":\"InvalidPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Ungültiger Username oder Passwort"));


    }

    @Test
    void authenticateAndGetToken_invalidCredentialsWithEmptyRequest() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Ungültiger Username oder Passwort"));


    }

    @Test
    void authenticateAndGetToken_missingFields() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Leerer RequestBody
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Ungültiger Username oder Passwort"));

        // Ensure authenticateAndGenerateToken is not called if request is invalid

    }

    @Test
    void registerUser_emptyUsername() throws Exception {
        String requestBody = "{\"username\":\"\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.username").value("Username muss angegeben werden"));
    }

    @Test
    void registerUser_tooShortUsername() throws Exception {
        String requestBody = "{\"username\":\"abc\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.username").value("Username muss zwischen 4 und 20 Zeichen groß sein"));
    }

    @Test
    void registerUser_tooLongUsername() throws Exception {
        String requestBody = "{\"username\":\"thisusernameiswaytool\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.username").value("Username muss zwischen 4 und 20 Zeichen groß sein"));
    }
    @Test
    void registerUser_withUsernameSizeOf20() throws Exception {
        String requestBody = "{\"username\":\"thisusernameiswaytoo\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Benutzer erfolgreich registriert"));
    }
    @Test
    void registerUser_withUsernameSizeOf4() throws Exception {
        String requestBody = "{\"username\":\"abcd\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Benutzer erfolgreich registriert"));
    }

    @Test
    void registerUser_missingOrEmptyPassword() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value("Es muss ein Passwort angegeben werden"));
    }

    @Test
    void registerUser_tooShortPassword() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"shortt3\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value("Passwort muss zwischen 8 und 20 Zeichen groß sein"));
    }

    @Test
    void registerUser_tooLongPassword() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"abcdefghijklmnopqrst6\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value("Passwort muss zwischen 8 und 20 Zeichen groß sein"));
    }
    @Test
    void registerUser_passwordWithoutNumbers() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPassword\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value("Passwort muss mindestens eine Zahl und einen Buchstaben enthalten"));
    }

    @Test
    void registerUser_passwordWithoutLetters() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"12345678\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value("Passwort muss mindestens eine Zahl und einen Buchstaben enthalten"));
    }

    @Test
    void registerUser_PasswortSizeOf20() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"abcdefghijklmnopqrs3\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Benutzer erfolgreich registriert"));
    }

    @Test
    void registerUser_missingOrEmptyEmail() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("E-Mail muss angegeben werden"));
    }

    @Test
    void registerUser_emailWithoutDomain() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"test@\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("Ungültige E-Mail angegeben"));
    }

    @Test
    void registerUser_emailWithoutUsername() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("Ungültige E-Mail angegeben"));
    }

    @Test
    void registerUser_missingOrEmptyFirstName() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.firstName").value("Vorname muss angegeben werden"));
    }

    @Test
    void registerUser_tooLongFirstName() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"abcdefghijklmnopqrstt\",\"lastName\":\"Doe\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.firstName").value("Vorname darf nur bis zu 20 Zeichen enthalten"));
    }

    @Test
    void registerUser_missingOrEmptyLastName() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.lastName").value("Nachname muss angegeben werden"));
    }

    @Test
    void registerUser_tooLongLastName() throws Exception {
        String requestBody = "{\"username\":\"validuser\",\"password\":\"ValidPass1\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"abcdefghijklmnopqrstt\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.lastName").value("Nachname darf nur bis zu 20 Zeichen enthalten"));
    }
}