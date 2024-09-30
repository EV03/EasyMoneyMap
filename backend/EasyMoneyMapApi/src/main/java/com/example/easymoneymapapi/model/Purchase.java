package com.example.easymoneymapapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Purchases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    public enum kategorie {
        FOOD,
        CLOTHING,
        LEISURE,
        MISC
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private String description;

    private String date;

    private double price;

    @Enumerated(EnumType.STRING)
    private kategorie category;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "purchase")
    private Set<UserKauf> userPurchases = new HashSet<>();

}
