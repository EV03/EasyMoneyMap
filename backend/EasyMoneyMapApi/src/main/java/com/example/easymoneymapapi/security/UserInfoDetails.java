package com.example.easymoneymapapi.security;

import com.example.easymoneymapapi.model.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserInfoDetails implements UserDetails {

    private String username;
    private String password;

    public UserInfoDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername(); // Oder das entsprechende Feld in UserInfo
        this.password = userInfo.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Falls keine Rollen implementiert sind
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementiere bei Bedarf
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementiere bei Bedarf
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implementiere bei Bedarf
    }

    @Override
    public boolean isEnabled() {
        return true; // Implementiere bei Bedarf
    }
}
