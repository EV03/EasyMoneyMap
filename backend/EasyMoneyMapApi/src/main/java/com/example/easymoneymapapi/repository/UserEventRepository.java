package com.example.easymoneymapapi.repository;

import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  UserEventRepository extends JpaRepository<UserEvent, Long> {

    Optional<UserEvent> findByUserIdAndEventId(long userId, long eventId );
    Optional<UserEvent> findByUserUsernameAndEventId(String username, long eventId);
    Boolean  existsByEventIdAndUserId(long eventId, Long userId);

    List<UserEvent> findByEventId(long eventId);

    @Query("SELECT ue FROM UserEvent ue WHERE ue.user.id = :id " +
            "AND (:title IS NULL OR ue.event.title LIKE %:title%) " +
            "AND (:status IS NULL OR ue.event.status = :status) " +
            "AND (:dateFrom IS NULL OR ue.event.dateFrom >= :dateFrom) " +
            "AND (:dateTo IS NULL OR ue.event.dateTo <= :dateTo)")
    List<UserEvent> findEventByFilters(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("status") Event.EventStatus status,
            @Param("dateFrom") String dateFrom,
            @Param("dateTo") String dateTo);
}
