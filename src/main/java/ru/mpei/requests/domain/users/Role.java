package ru.mpei.requests.domain.users;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Role implements GrantedAuthority, Serializable { //Role system for the users
    USER, ADMIN, MODER, EXECUTER, CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}