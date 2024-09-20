package com.example.easymoneymapapi.service;

import com.example.easymoneymapapi.exception.UserAlreadyExistsException;
import com.example.easymoneymapapi.security.UserInfoDetails;
import com.example.easymoneymapapi.model.UserInfo;
import com.example.easymoneymapapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserInfo registerUser(UserInfo user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username wird bereits verwendet");
        }
        if ( userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Diese Email wird bereits verwendet");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        if (encodedPassword == null || encodedPassword.isEmpty()) {
            throw new IllegalStateException("Password encoding failed");
        }
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserInfoDetails(user);
    }
}
