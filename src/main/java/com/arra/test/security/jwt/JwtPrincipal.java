package com.arra.test.security.jwt;

import java.security.Principal;

public class JwtPrincipal implements Principal {
    private String username;
    private String[] authorities;


    public JwtPrincipal(String username) {
        this(username, null);
    }

    public JwtPrincipal(String username, String[] authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return username;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
