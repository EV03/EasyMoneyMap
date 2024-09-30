package com.example.easymoneymapapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_purchases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKauf {
    public enum UserPurchaseStatus {
        OPEN,
        PAID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    private double toPay;

    private double paid;

    @Enumerated (EnumType.STRING)
    private UserPurchaseStatus status;
}
