package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.exception.EventNotFoundException;
import com.example.easymoneymapapi.exception.UserAlreadyExistsException;
import com.example.easymoneymapapi.exception.UserInEventNotFoundException;
import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.repository.EventRepository;
import com.example.easymoneymapapi.repository.UserEventRepository;
import com.example.easymoneymapapi.repository.UserRepository;
import com.example.easymoneymapapi.security.EventSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserEventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventRepository userEventRepository;

    @Mock
    EventSecurity eventSecurity;

    @InjectMocks
    private UserEventService userEventService;

    public UserEventServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    UserInfo user ;
    Event event;

    @BeforeEach
    public void setUp() {

        user = new UserInfo();
        user.setId(1L);
        user.setUsername("testuser");
        event = new Event();
        event.setId(1L);
    }

    @Test
    public void testAddUserToEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userEventRepository.existsByEventIdAndUserId(1L, 1L)).thenReturn(false);
        doNothing().when(eventSecurity).validateAddPermission(1L, "requester", 1L);
        // mock des UserEvent Objekts das zurückgegeben wird vom Repository
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);
        UserInfo result = userEventService.addUserToEvent(1L, 1L, "requester");
        assertEquals(user, result);
    }

    @Test
    public void testRemoveUserFromEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        when(eventSecurity.validateRemovePermission(1L, "requester", 1L))
                .thenReturn(userEvent);
        userEventService.removeUserFromEvent(1L, 1L, "requester");
        verify(userEventRepository, times(1)).delete(userEvent);
    }

    @Test
    public void testSetUserRole() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        when(eventSecurity.validateEditRolePermission(1L, "requester", 1L))
                .thenReturn(userEvent);
        userEventService.setUserRole(1L, 1L, "requester", UserEvent.Role.Admin);
        verify(userEventRepository, times(1)).save(userEvent);
    }

    @Test
    public void findEventOrThrow_exception() {

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> {
            userEventService.findEventOrThrow(1L);
        });

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userEventService.findUserOrThrow(1L);
        });

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userEventService.findUserOrThrow("testuser");
        });
    }
}
