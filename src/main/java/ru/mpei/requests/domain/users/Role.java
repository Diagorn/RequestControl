package ru.mpei.requests.domain.users;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority { //Role system for the users
    USER, ADMIN, MODER, EXECUTER, CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}