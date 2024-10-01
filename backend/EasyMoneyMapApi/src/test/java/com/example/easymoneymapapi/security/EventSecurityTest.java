

package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.exception.UnauthorizedAccessException;
import com.example.easymoneymapapi.exception.UserAlreadyExistsException;
import com.example.easymoneymapapi.exception.UserInEventNotFoundException;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.repository.UserEventRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class EventSecurityTest {

    @Mock
    private UserEventRepository userEventRepository;

    @InjectMocks
    private EventSecurity eventSecurity;

    public EventSecurityTest() {
        MockitoAnnotations.openMocks(this);
    }

    long eventId;
    long userId;
    String requesterUsername;
    UserEvent requesterUserEvent;

    @BeforeEach
    public void setUp() {

        eventId = 1L;
        userId = 2L;
        requesterUsername = "requester";
        requesterUserEvent = new UserEvent();
    }

    @Test
    public void testValidateAddPermission_success() {
        requesterUserEvent.setRole(new Role("Admin"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        when(userEventRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(false);

        // verifiziert, dass keine exception geworfen wurde
        eventSecurity.validateAddPermission(eventId, requesterUsername, userId);

        verify(userEventRepository).findByUserUsernameAndEventId(requesterUsername, eventId);
        verify(userEventRepository).existsByEventIdAndUserId(eventId, userId);
    }

    @Test
    public void testValidateAddPermission_userAlreadyExists() {

        requesterUserEvent.setRole(new Role("Admin"));

        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));
        // Benutzer ist bereits im Event
        when(userEventRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            eventSecurity.validateAddPermission(eventId, requesterUsername, userId);
        });
    }

    @Test
    public void testValidateAddPermission_noPermissionToAddUser() {
        // Mock den Requester als "Member" (keine Berechtigung zum Hinzufügen)
        requesterUserEvent.setRole(new Role("Member"));

        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        when(userEventRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(false);

        assertThrows(UnauthorizedAccessException.class, () -> {
            eventSecurity.validateAddPermission(eventId, requesterUsername, userId);
        });
    }

    @Test
    public void testValidateRemovePermission_success() {

        requesterUserEvent.setRole(new Role("Admin"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        UserEvent userToRemoveEvent = new UserEvent();
        userToRemoveEvent.setRole(new Role("Member"));
        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.of(userToRemoveEvent));

        UserEvent result = eventSecurity.validateRemovePermission(eventId, requesterUsername, userId);
        assertEquals(userToRemoveEvent, result);
    }

    @Test
    public void testValidateRemovePermission_noPermissionToRemoveUser() {
        requesterUserEvent.setRole(new Role("Member"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        UserEvent userToRemoveEvent = new UserEvent();
        userToRemoveEvent.setRole(new Role("Member"));
        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.of(userToRemoveEvent));

        assertThrows(UnauthorizedAccessException.class, () -> {
            eventSecurity.validateRemovePermission(eventId, requesterUsername, userId);
        });
    }

    @Test
    public void testValidateRemovePermission_UserNotInEvent() {
        requesterUserEvent.setRole(new Role("Admin"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.empty());

        assertThrows(UserInEventNotFoundException.class, () -> {
            eventSecurity.validateRemovePermission(eventId, requesterUsername, userId);
        });
    }

    @Test
    public void testValidateEditRolePermission_success() {
        requesterUserEvent.setRole(new Role("Admin"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        UserEvent userToEdit = new UserEvent();
        userToEdit.setRole(new Role("Member"));
        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.of(userToEdit));

        UserEvent result = eventSecurity.validateEditRolePermission(eventId, requesterUsername, userId);
        assertEquals(userToEdit, result);
    }

    @Test
    public void testValidateEditRolePermission_noPermissionToEditRole() {
        requesterUserEvent.setRole(new Role("Member"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        UserEvent userToEdit = new UserEvent();
        userToEdit.setRole(new Role("Member"));
        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.of(userToEdit));

        assertThrows(UnauthorizedAccessException.class, () -> {
            eventSecurity.validateEditRolePermission(eventId, requesterUsername, userId);
        });
    }

    @Test
    public void testValidateEditRolePermission_UserNotInEvent() {
        requesterUserEvent.setRole(new Role("Admin"));
        when(userEventRepository.findByUserUsernameAndEventId(requesterUsername, eventId))
                .thenReturn(Optional.of(requesterUserEvent));

        when(userEventRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.empty());

        assertThrows(UserInEventNotFoundException.class, () -> {
            eventSecurity.validateEditRolePermission(eventId, requesterUsername, userId);
        });
    }
}
