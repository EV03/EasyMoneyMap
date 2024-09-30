package com.example.easymoneymapapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Events")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    public enum EventStatus {
        ACTIVE,
        PLANNED,
        COMPLETED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private LocalDate dateFrom;

    private LocalDate dateTo;

    private String title;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event")
    private Set<UserEvent> userEvents = new HashSet<>();

    @OneToMany(mappedBy = "event")
    private Set<Purchase> purchases = new HashSet<>();
}
