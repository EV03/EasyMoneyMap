package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.dto.EventDTO;
import com.example.easymoneymapapi.dto.UserInfoDTO;
import com.example.easymoneymapapi.exception.EventNotFoundException;
import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.repository.EventRepository;
import com.example.easymoneymapapi.repository.UserEventRepository;
import com.example.easymoneymapapi.repository.UserRepository;
import com.example.easymoneymapapi.security.CreatorRole;
import com.example.easymoneymapapi.security.Role;
import com.example.easymoneymapapi.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.easymoneymapapi.security.EventSecurity;

import java.util.List;
import java.util.Optional;


/**
 * Service für die Verwaltung von User zu den Events
 * hier ist die Logik für die Berechtigungen der User auf die Events
 * z.B. User hinzufügen, User entfernen, Userrolle ändern
 *
 */

@Service
public class UserEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private EventSecurity eventSecurity;

    @Value("${creator-event-default-role}")
    String creatorEventDefaultRole;

    @Value("${add-User-Default-Role}")
    String memberEventDefaultRole;

    @Transactional
    public  void addCreatorOfEvent(long eventId, String creatorUsername) {
        Event event = findEventOrThrow(eventId);
        UserInfo creatorUser = findUserOrThrow(creatorUsername);
        saveUserInEvent(event, creatorUser, new Role(creatorEventDefaultRole));
    }
    @Transactional
    public UserInfo addUserToEvent(long eventId, Long userId, String requesterUsername ) {
        Event event = findEventOrThrow(eventId);
        UserInfo userToAdd = findUserOrThrow(userId);
        eventSecurity.validateAddPermission (eventId, requesterUsername , userId);
        return saveUserInEvent(event, userToAdd, new Role(memberEventDefaultRole)).getUser();
    }
    @Transactional
    public void removeUserFromEvent(long eventId, Long userId, String requesterUsername ) {
        Event event = findEventOrThrow(eventId);
        UserInfo userToRemove= findUserOrThrow(userId);
        UserEvent userEventToRemove = eventSecurity.validateRemovePermission (eventId, requesterUsername , userId);
        userEventRepository.delete(userEventToRemove);
    }
    @Transactional
    public void editUserRole(long eventId, Long userId, String requesterUsername) {
        Event event = findEventOrThrow(eventId);
        UserInfo userToEdit = findUserOrThrow(userId);
        UserEvent userEvent = eventSecurity.validateEditRolePermissionAndEditRole (eventId, requesterUsername , userId);
        userEventRepository.save(userEvent);
    }
    @Transactional
    public UserEvent saveUserInEvent (Event event, UserInfo user, Role role) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setRole(role);
        return  userEventRepository.save(userEvent);
    }

    public List<EventDTO> getEventsOfUser(String requesterUsername, String title, Event.EventStatus status,
                                              String dateFrom, String dateTo) {
        UserInfo user = findUserOrThrow(requesterUsername);

        List<UserEvent> userEvents = userEventRepository
                .findEventByFilters(user.getId(), title, status, dateFrom, dateTo);

        if (userEvents.isEmpty()) {
            throw new EventNotFoundException("Keine Events gefunden");
        }

        return userEvents.stream()
                .map(UserEvent::getEvent)
                .toList().stream().map(EventDTO::mapToEventDTO).toList();
    }

    public List<UserInfoDTO> getUsersOfEvent(long eventId) {
        Event event = findEventOrThrow(eventId);
        List<UserEvent> userEvents = userEventRepository.findByEventId(eventId);
        return userEvents.stream().map(UserEvent::mapToUserInfoDTO).toList();
    }

    @Transactional
    public Event deleteEvent(long eventId, String requesterUsername) {
        Event event = findEventOrThrow(eventId);
        eventSecurity.validateDeleteEventPermission(eventId, requesterUsername);
        return event;
    }

    @Transactional
    public void leaveEvent(long eventId, String requesterUsername) {

        Event event = findEventOrThrow(eventId);
        UserInfo requesterUser = findUserOrThrow(requesterUsername);

        UserEvent requestUserEvent = eventSecurity.validateLeaveEventPermission(eventId, requesterUsername);

        List<UserEvent> eventParticipants = userEventRepository.findByEventId(eventId);

        if (eventParticipants.size() == 1) {
            eventRepository.delete(event);
            return;
        }

        // falls der Ersteller das Event verlässt, wird ein neuer Ersteller gesucht
        // neuer Ersteller, ist der erste in der Liste der Teilnehmer (vorerst)
        // vergleich mit instanceof da spring den creatorEventDefaultRole mit null initialisiert
        UserRole requesterRole = requestUserEvent.getRole().getRoleLogic();
        if (requesterRole instanceof CreatorRole) {
            // Suche nach einem neuen Ersteller (Member oder Admin)
            Optional<UserEvent> newCreator = eventParticipants.stream()
                    .filter(ue -> !ue.getUser().equals(requesterUser))
                    .findFirst();

            newCreator.ifPresent(creator -> {
                creator.setRole(new Role(creatorEventDefaultRole));
                userEventRepository.save(creator);
            });
        }
        // user verlässt Event unabhängig von seiner Rolle
        userEventRepository.delete(requestUserEvent);
    }

    public Event findEventOrThrow(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event mit Id: " + eventId + " nicht gefunden"));
    }

    public UserInfo findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User mit UserId: " + userId + " nicht gefunden"));
    }

    public UserInfo findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User mit Username: " + username + " nicht gefunden"));
    }

}
