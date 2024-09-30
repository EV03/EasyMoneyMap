package com.example.easymoneymapapi.repository;


import com.example.easymoneymapapi.model.Event;
import com.example.easymoneymapapi.model.UserEvent;
import com.example.easymoneymapapi.model.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserEventRepositoryTest {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private UserInfo userInfo;
    private Event event;

    @BeforeEach
    public void setUp() {

        userInfo = new UserInfo();
        userInfo.setUsername("testuser");
        userRepository.save(userInfo);

        event = new Event();
        event.setTitle("Testevent");
        eventRepository.save(event);
    }

    @Test
    public void findByUserIdAndEventId() {

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(userInfo);
        userEvent.setEvent(event);
        userEventRepository.save(userEvent);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserIdAndEventId(userInfo.getId(), event.getId());

        assertThat(foundUserEvent).isPresent();
        assertThat(foundUserEvent.get()).isEqualTo(userEvent);

    }
    @Test
    public void findByUserIdAndEventId_EventInUserNotFound() {
        Event anotherEvent = new Event();
        anotherEvent.setTitle("AnotherEvent");
        eventRepository.save(anotherEvent);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserIdAndEventId(userInfo.getId(), anotherEvent.getId());

        assertThat(foundUserEvent).isNotPresent();
    }

    @Test
    public void findByUserIdAndEventId_userInEventNotFound() {
        UserInfo anotherUser = new UserInfo();
        anotherUser.setUsername("anotherUser");
        userRepository.save(anotherUser);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserIdAndEventId(anotherUser.getId(), event.getId());

        assertThat(foundUserEvent).isNotPresent();
    }

    // jetzt die selben tests nochmal wo man den usernamen statt der id verwendet

    @Test
    public void findByUserUsernameAndEventId() {

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(userInfo);
        userEvent.setEvent(event);
        userEventRepository.save(userEvent);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserUsernameAndEventId(userInfo.getUsername(), event.getId());

        assertThat(foundUserEvent).isPresent();
        assertThat(foundUserEvent.get()).isEqualTo(userEvent);

    }
    @Test
    public void findByUserUsernameAndEventId_EventInUserNotFound() {
        Event anotherEvent = new Event();
        anotherEvent.setTitle("AnotherEvent");
        eventRepository.save(anotherEvent);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserUsernameAndEventId(userInfo.getUsername(), anotherEvent.getId());

        assertThat(foundUserEvent).isNotPresent();
    }

    @Test
    public void findByUserUsernameAndEventId_userInEventNotFound() {
        UserInfo anotherUser = new UserInfo();
        anotherUser.setUsername("anotherUser");
        userRepository.save(anotherUser);

        Optional<UserEvent> foundUserEvent = userEventRepository
                .findByUserUsernameAndEventId(anotherUser.getUsername(), event.getId());

        assertThat(foundUserEvent).isNotPresent();
    }

    // findByfilters noch testen mit allen möglichen kombinationen von filtern

    // findByEventId returnt liste von allen userEvents zu einem event
}
