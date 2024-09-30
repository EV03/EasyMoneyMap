package com.example.easymoneymapapi.repository;


import com.example.easymoneymapapi.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


// testet Datenbankzugriff
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Optional<UserInfo> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent(), "Benutzer sollte gefunden werden");
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void whenExistsByUsername_thenReturnTrue() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Boolean exists = userRepository.existsByUsername("testuser");

        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    public void whenNotFindByEmail_thenReturnTrue() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Boolean exists = userRepository.existsByEmail("est@example.com");

        assertThat(exists).isFalse();
    }

}
