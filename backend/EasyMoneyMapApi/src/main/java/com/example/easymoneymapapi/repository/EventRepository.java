package com.example.easymoneymapapi.repository;

import com.example.easymoneymapapi.exception.EventNotFoundException;
import com.example.easymoneymapapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(long id) throws EventNotFoundException;
    List<Event> findByTitle(String title);

    List<Event> findByStatus(Event.EventStatus status);

    List<Event> findByDateFrom(LocalDate dateFrom);

    List<Event> findByDateTo(LocalDate dateTo);
    Boolean existsByTitle(String title);
    // exist bei dateFrom
    Boolean existsByDateFrom(LocalDate dateFrom);
    // filtert nach Titel, Status, Datum von und Datum bis
    List<Event> findByTitleContainingIgnoreCaseAndStatusAndDateFromGreaterThanEqualAndDateToLessThanEqual
            (String title, Event.EventStatus status, LocalDate dateFrom, LocalDate dateTo);

}