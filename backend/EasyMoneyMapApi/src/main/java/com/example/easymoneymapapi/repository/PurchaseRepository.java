package com.example.easymoneymapapi.repository;

import com.example.easymoneymapapi.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
