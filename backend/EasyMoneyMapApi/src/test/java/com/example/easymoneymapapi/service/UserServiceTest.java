package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.exception.UserAlreadyExistsException;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.repository.UserRepository;
import com.example.easymoneymapapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.easymoneymapapi.security.UserInfoDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_success() {
        UserInfo user = new UserInfo();
        user.setEmail("test@gmail.com");
        user.setFirstName("tester");
        user.setLastName("testet");
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedpassword");
        when(userRepository.save(any(UserInfo.class))).thenReturn(user);

        UserInfo result = userService.registerUser(user);
        assertNotNull(result);
        assertEquals("encodedpassword", result.getPassword());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_userAlreadyExists() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");

        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));

        verify(userRepository, never()).save(any(UserInfo.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        UserInfo user = new UserInfo();
        user.setEmail("testuser@gmail.com");

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));

        verify(userRepository, never()).save(any(UserInfo.class));
    }

    @Test
    void testRegisterUser_withNoValidPasswordEncoding() {

        UserInfo user = new UserInfo();
        user.setEmail("test@gmail.com");
        user.setFirstName("tester");
        user.setLastName("testet");
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        // fail in password encoding
        when(passwordEncoder.encode(any(String.class))).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> userService.registerUser(user));

        verify(userRepository, never()).save(any(UserInfo.class));
    }

    @Test
    void loadByUsername_success() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserInfoDetails userDetails = (UserInfoDetails) userService.loadUserByUsername("testuser");
        assertEquals("testuser", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_userNotFound() {

        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistentuser"));

        verify(userRepository, times(1)).findByUsername("nonexistentuser");

    }

    @Test // testet Konstruktor von UserInfoDetails
    void testUserInfoDetails() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");

        UserInfoDetails userDetails = new UserInfoDetails(user);

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedpassword", userDetails.getPassword());
    }

}
