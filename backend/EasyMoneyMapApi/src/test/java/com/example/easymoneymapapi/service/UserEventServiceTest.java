package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.dto.UserInfoDTO;
import com.example.easymoneymapapi.exception.EventNotFoundException;
import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.repository.EventRepository;
import com.example.easymoneymapapi.repository.UserEventRepository;
import com.example.easymoneymapapi.repository.UserRepository;
import com.example.easymoneymapapi.security.EventSecurity;
import com.example.easymoneymapapi.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
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

    UserInfo user;
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
    public void testEditUserRole() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        when(eventSecurity.validateEditRolePermissionAndEditRole(1L, "requester", 1L))
                .thenReturn(userEvent);
        userEventService.editUserRole(1L, 1L, "requester");
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

    // leave event
    @Test
    public void testLeaveEvent_OnlyOneUserInEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        when(userEventRepository.findByEventId(1L)).thenReturn(new ArrayList<>(List.of(userEvent)));
        when(eventSecurity.validateLeaveEventPermission(1L, "testuser"))
                .thenReturn(userEvent);
        userEventService.leaveEvent(1L, user.getUsername());
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    public void testLeaveEvent_MoreThanOneUserAndUserIsCreator() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setRole(new Role("CREATOR")); // value aus properties file werden nicht korrket gelesen
        UserEvent userEvent2 = new UserEvent();
        UserInfo user2 = new UserInfo();
        userEvent2.setUser(user2);
        when(userEventRepository.findByEventId(1L)).thenReturn(new ArrayList<>(List.of(userEvent, userEvent2)));
        when(eventSecurity.validateLeaveEventPermission(1L, "testuser"))
                .thenReturn(userEvent);
        userEventService.leaveEvent(1L, user.getUsername());
        verify(userEventRepository, times(1)).save(userEvent2);

    }

    @Test
    public void testLeaveEvent_MoreThanOneUserAndUserIsNotCreator() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setRole(new Role("MEMBER"));
        UserEvent userEvent2 = new UserEvent();
        UserInfo user2 = new UserInfo();
        userEvent2.setUser(user2);
        when(userEventRepository.findByEventId(1L)).thenReturn(new ArrayList<>(List.of(userEvent, userEvent2)));
        when(eventSecurity.validateLeaveEventPermission(1L, "testuser"))
                .thenReturn(userEvent);
        userEventService.leaveEvent(1L, user.getUsername());
        verify(userEventRepository, times(1)).delete(userEvent);
    }

    @Test
    public void getUserOfEvents() {
        UserEvent userEvent2 = new UserEvent();
        UserEvent userEvent3 = new UserEvent();
        UserInfo user2 = new UserInfo("username2", "password", "email", "firstName",
                "lastName");
        UserInfo user3 = new UserInfo("username3", "password", "email", "firstName",
                "lastName");
        userEvent2.setUser(user2);
        userEvent3.setUser(user3);
        userEvent2.setRole(new Role("MEMBER"));
        userEvent3.setRole(new Role("MEMBER"));

        when(userEventRepository.findByEventId(1L)).thenReturn(new ArrayList<>(List.of(userEvent2,userEvent3)));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Collection<UserInfoDTO> result = userEventService.getUsersOfEvent(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void getEventsOfUser() {

    }

}





