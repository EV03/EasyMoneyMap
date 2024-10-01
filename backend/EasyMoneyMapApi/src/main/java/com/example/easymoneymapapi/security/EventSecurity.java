package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.exception.UnauthorizedAccessException;
import com.example.easymoneymapapi.exception.UserAlreadyExistsException;
import com.example.easymoneymapapi.exception.UserInEventNotFoundException;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.repository.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * checkt ob der Benutzer die Berechtigung hat, eine Aktion auf ein Event auszuführen
 * z.B. Benutzer hinzufügen, Benutzer entfernen, Benutzerrolle ändern
 */
@Component
public class EventSecurity {

    @Autowired
    private UserEventRepository userEventRepository;

    private UserEvent getUserEventOrThrow(Long userId, long eventId) {
        return userEventRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new UserInEventNotFoundException("Benutzer mit ID " + userId
                        + " ist nicht im Event."));
    }

    private UserEvent getUserEventOrThrow(String username, long eventId) {
        return userEventRepository.findByUserUsernameAndEventId(username, eventId)
                .orElseThrow(() -> new UnauthorizedAccessException("Benutzer " + username
                        + " ist nicht im Event."));
    }

    public void validateAddPermission (long eventId, String requesterUsername , Long userId) {
        UserEvent requesterUserEvent = getUserEventOrThrow(requesterUsername, eventId);

        if (userEventRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new UserAlreadyExistsException("Benutzer mit ID " + userId + " ist bereits im Event.");
        }

        if (!requesterUserEvent.getRole().getRoleLogic().canAddUser()) {
            throw new UnauthorizedAccessException("Benutzer " + requesterUsername
                    + " hat keine Berechtigung, Benutzer hinzuzufügen.");
        }

    }
    public UserEvent validateRemovePermission (long eventId, String requesterUsername, Long userId) {
        UserEvent requesterUserEvent = getUserEventOrThrow(requesterUsername, eventId);
        UserEvent userToRemoveEvent = getUserEventOrThrow(userId, eventId);

        if (!requesterUserEvent.getRole().getRoleLogic().canRemoveUser(userToRemoveEvent.getRole().getRoleLogic())) {
            throw new UnauthorizedAccessException("Benutzer " + requesterUsername
                    + " kann Benutzer mit ID " + userId + " nicht entfernen.");
        }

        return userToRemoveEvent;
    }

    public UserEvent validateEditRolePermission (long eventId, String requesterUsername, Long userId) {
        UserEvent requesterUserEvent = getUserEventOrThrow(requesterUsername, eventId);
        UserEvent userToEdit = getUserEventOrThrow(userId, eventId);

        if (!requesterUserEvent.getRole().getRoleLogic().canEditUserRole(userToEdit.getRole().getRoleLogic())) {
            throw new UnauthorizedAccessException("Benutzer " + requesterUsername
                    + " kann die Rolle von Benutzer mit ID " + userId + " nicht ändern.");
        }

        return userToEdit;
    }

    public void validateDeleteEventPermission (long eventId, String requesterUsername) {
        UserEvent requesterUserEvent = getUserEventOrThrow(requesterUsername, eventId);

        if (!requesterUserEvent.getRole().getRoleLogic().canDeleteEvent()) {
            throw new UnauthorizedAccessException("Benutzer " + requesterUsername
                    + " hat keine Berechtigung, das Event zu löschen.");
        }
    }

    public UserEvent validateLeaveEventPermission (long eventId, String requesterUsername) {

        return getUserEventOrThrow(requesterUsername, eventId);

    }
}
