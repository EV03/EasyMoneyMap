package com.example.easymoneymapapi.controller;

import com.example.easymoneymapapi.dto.EventDTO;
import com.example.easymoneymapapi.dto.UserInfoDTO;
import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.service.EventService;
import com.example.easymoneymapapi.service.UserEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    UserEventService userEventService;

    @GetMapping("/")
    public ResponseEntity<List<EventDTO>> getEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Event.EventStatus status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            Principal principal
    ) {
       List<EventDTO> events = userEventService.getEventsOfUser(principal.getName(), title, status, dateFrom, dateTo);

       return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserInfoDTO>> getUsers(@PathVariable long id) {
        List<UserInfoDTO> users = userEventService.getUsersOfEvent(id);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/")
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventReq, Principal principal) {
        Event createdEvent = eventService.createEvent(eventReq.mapToEvent(), principal.getName());
        return ResponseEntity.ok(EventDTO.mapToEventDTO(createdEvent));
    }

    // Event löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent( @PathVariable long id, Principal principal) {
        eventService.deleteEvent(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/addUser")
    public ResponseEntity<UserInfo> addUserToEvent(@PathVariable long id, @RequestBody Long userId,
                                                   Principal principal) {
        UserInfo addedUser = userEventService.addUserToEvent(id, userId, principal.getName());
        return ResponseEntity.ok(addedUser);
    }

    @DeleteMapping("/{id}/removeUser")
    public ResponseEntity<UserInfo> removeUserFromEvent
    ( @PathVariable long id, @RequestBody Long userId, Principal principal) {
        userEventService.removeUserFromEvent(id, userId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Event bearbeiten , title, dateFrom, dateTo, status
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> editEvent( @PathVariable long id, @Valid @RequestBody EventDTO event,
                                            Principal principal) {
        Event updatedEvent = eventService.editEvent(id, event.mapToEvent(), principal.getName());
        return ResponseEntity.ok(EventDTO.mapToEventDTO(updatedEvent));
    }
    @PutMapping("/{id}/editUserRole")
    public ResponseEntity<String> editUserRole( @PathVariable long id, @RequestBody Long userId,
                                                 Principal principal) {
        userEventService.editUserRole(id, userId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
