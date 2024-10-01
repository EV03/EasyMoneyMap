package com.example.easymoneymapapi.repository;


import com.example.easymoneymapapi.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    @Test
    @DisplayName("Test findAll")

    @BeforeEach
    public void setUp() {
        // Common Event setup for multiple tests
        event1 = createEvent("Spring Boot Conference",
                LocalDate.parse("01.01.2024", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse("05.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                Event.EventStatus.ACTIVE);

        event2 = createEvent("Spring Framework Workshop",
                LocalDate.parse("10.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse("15.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                Event.EventStatus.ACTIVE);

        event3 = createEvent("Spring Summit",
                LocalDate.parse("20.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse("25.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                Event.EventStatus.PLANNED);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
    }

    private Event createEvent(String title, LocalDate dateFrom, LocalDate dateTo, Event.EventStatus status) {
        Event event = new Event();
        event.setTitle(title);
        event.setDateFrom(dateFrom);
        event.setDateTo(dateTo);
        event.setStatus(status);
        return event;
    }

    @Test
    @DisplayName("Test findAll")
    public void testFindAllEvents() {
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(3);
        assertThat(events).extracting(Event::getTitle).contains("Spring Boot Conference", "Spring Framework Workshop",
                "Spring Summit");
    }

    @Test
    @DisplayName("Should find event by status")
    public void testFindByStatus() {
        List<Event> foundEvents = eventRepository.findByStatus(Event.EventStatus.ACTIVE);
        assertThat(foundEvents).hasSize(2);
        assertThat(foundEvents).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Spring Boot Conference", "Spring Framework Workshop");
    }

    @Test
    @DisplayName("Should find event by title")
    public void testFindEventByTitle() {
        List<Event> foundEvents = eventRepository.findByTitle("Spring Boot Conference");
        assertThat(foundEvents).hasSize(1);
        assertThat(foundEvents.get(0).getTitle()).isEqualTo("Spring Boot Conference");
    }

    @Test
    @DisplayName("Should check if event exists by title")
    public void testExistsByTitle() {
        Boolean exists = eventRepository.existsByTitle("Spring Boot Conference");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find event by ID")
    public void testFindById() {
        Optional<Event> foundEvent = eventRepository.findById(event1.getId());
        assertThat(foundEvent).isPresent();
        assertThat(foundEvent.get().getTitle()).isEqualTo("Spring Boot Conference");
    }

    @Test
    @DisplayName("Should find event by dateFrom")
    public void testFindByDateFrom() {
        List<Event> foundEvents = eventRepository.findByDateFrom(LocalDate.parse("01.01.2024",
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(foundEvents).hasSize(1);
        assertThat(foundEvents.get(0).getTitle()).isEqualTo("Spring Boot Conference");
    }

    @Test
    @DisplayName("Should find event by dateTo")
    public void testFindByDateTo() {
        List<Event> foundEvents = eventRepository.findByDateTo(LocalDate.parse("05.01.2024",
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(foundEvents).hasSize(1);
        assertThat(foundEvents.get(0).getTitle()).isEqualTo("Spring Boot Conference");
    }

    @Test
    @DisplayName("Should check if event exists by dateFrom")
    public void testExistsByDateFrom() {
        Boolean exists = eventRepository.existsByDateFrom(LocalDate.parse("01.01.2024",
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("Should find events by title (case insensitive), status, and date range")
    public void testFindByTitleStatusAndDateRange() {


        List<Event> foundEvents = eventRepository
                .findByTitleContainingIgnoreCaseAndStatusAndDateFromGreaterThanEqualAndDateToLessThanEqual(
                        "spring", Event.EventStatus.ACTIVE, LocalDate.parse("01.01.2024",
                                DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalDate.parse("15.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        assertThat(foundEvents).hasSize(2);
        assertThat(foundEvents).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Spring Boot Conference", "Spring Framework Workshop");
    }

    @Test
    @DisplayName("Should return empty list when no events match the filters")
    public void testNoEventsFoundWithDateRange() {

        List<Event> foundEvents = eventRepository
                .findByTitleContainingIgnoreCaseAndStatusAndDateFromGreaterThanEqualAndDateToLessThanEqual(
                        "spring", Event.EventStatus.ACTIVE, LocalDate.parse("02.01.2024",
                                DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                         LocalDate.parse("14.01.2024",DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        assertThat(foundEvents).isEmpty();
    }
     // filter methode kann rausgenommen werden
}
