package com.example.easymoneymapapi.dto;


import com.example.easymoneymapapi.model.Event;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

@Data
public class EventDTO {


    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{2}", message = "Datum muss im Format dd.MM.yy sein")
    private String dateFrom;

    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{2}", message = "Datum muss im Format dd.MM.yy sein")
    private String dateTo;

    @Size(min = 1, max = 30, message = "Titel muss zwischen 1 und 30 Zeichen lang sein")
    private String title;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");


    public Event  mapToEvent(){
        Event event = new Event();
        try {
            event.setDateFrom(LocalDate.parse(this.dateFrom, DATE_FORMATTER));
            event.setDateTo(LocalDate.parse(this.dateTo, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Datum konnte nicht geparst werden", e);
        }
        event.setTitle(this.title);

        return event;
    }

    public static EventDTO mapToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setDateFrom(event.getDateFrom().format(DATE_FORMATTER));
        eventDTO.setDateTo(event.getDateTo().format(DATE_FORMATTER));
        eventDTO.setTitle(event.getTitle());
        return eventDTO;
    }

}
