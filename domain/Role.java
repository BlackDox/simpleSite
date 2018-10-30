package com.mysite.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    NEWBIE, USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
