package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.exception.EventNotFoundException;
import com.example.easymoneymapapi.exception.EventWithNameAndDateAlreadyExists;
import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.repository.EventRepository;
import com.example.easymoneymapapi.repository.UserEventRepository;
import com.example.easymoneymapapi.repository.UserKaufRepository;
import com.example.easymoneymapapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// todo: implementieren
@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired UserEventService userEventService;

    @Autowired
    private UserEventRepository userEventRepository;


    @Autowired
    private UserKaufRepository userKaufRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(Event event, String requesterUsername) {


        if (event.getDateTo().isBefore(event.getDateFrom())) {
            throw new IllegalArgumentException("Das Enddatum muss nach dem Startdatum liegen");
        } else if (event.getDateTo().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Das EndDatum darf nicht in der Vergangenheit liegen");
        }

        LocalDate currentDate = LocalDate.now();

        if (event.getDateFrom().isAfter(currentDate)) {
                event.setStatus(Event.EventStatus.PLANNED);
        } else {
                event.setStatus(Event.EventStatus.ACTIVE);
        }

        if (eventRepository.existsByTitle(event.getTitle()) && eventRepository.existsByDateFrom(event.getDateFrom())) {
                throw new EventWithNameAndDateAlreadyExists("Event mit diesem Namen und Datum existiert bereits");
        }

        Event savedEvent  = eventRepository.save(event);
        userEventService.addCreatorOfEvent(savedEvent.getId(), requesterUsername);
        return savedEvent ;
        }

        // ab hier in userEventService klasse verschieben
    @Transactional
    public void deleteEvent(long eventId, String byUsername) {

        Event event = userEventService.deleteEvent(eventId, byUsername);
        eventRepository.deleteById(eventId);
    }
    @Transactional
    public Event editEvent(long id, Event event, String username) {

        Event eventToEdit = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event nicht gefunden"));

        if(event.getDateTo().isBefore(event.getDateFrom())) {
            throw new IllegalArgumentException("Das Enddatum muss nach dem Startdatum liegen");
        }

        if(event.getDateFrom().isAfter(LocalDate.now()) && !eventToEdit.getPurchases().isEmpty()) {
            throw new IllegalArgumentException("StartDatum kann nicht in der Zukunft liegen, " +
                    "wenn es bereits Käufe gibt");
        }

        eventToEdit.setTitle(event.getTitle());
        eventToEdit.setDateFrom(event.getDateFrom());
        eventToEdit.setDateTo(event.getDateTo());

      //  Enddatum in der Vergangenheit und alle Transaktionen abgeschlossen
        if (event.getDateTo().isBefore(LocalDate.now()) & areAllTransactionsSettled(eventToEdit)) {
            eventToEdit.setStatus(Event.EventStatus.COMPLETED);

            //Startdatum in der Zukunft und keine Käufe
        } else if (event.getDateFrom().isAfter(LocalDate.now()) && eventToEdit.getPurchases().isEmpty()) {
            eventToEdit.setStatus(Event.EventStatus.PLANNED);

        } else {
            eventToEdit.setStatus(Event.EventStatus.ACTIVE);
        }

        return eventRepository.save(eventToEdit);
    }
    // erstmal auf true gesetzt, da die Methode noch nicht implementiert ist.
    // guckt, ob für jeden Kauf in einem Event, alle Transaktionen des Kaufes abgeschlossen sind
    private boolean areAllTransactionsSettled(Event event ) {
        return true ;
    }

}