package com.arra.test.security.jwt;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Arrays;

public class JWTSecurityContext implements SecurityContext {
    private SecurityContext delegate;
    private JwtPrincipal jwtPrincipal;

    public JWTSecurityContext(SecurityContext delegate, JwtPrincipal jwt) {
        this.delegate = delegate;
        this.jwtPrincipal = jwt;
    }

    @Override
    public Principal getUserPrincipal() {
        return jwtPrincipal;
    }

    @Override
    public boolean isUserInRole(String s) {
        String[] authorities = jwtPrincipal.getAuthorities();
        if(authorities == null || authorities.length == 0) {
            return true;
        }
        return Arrays.stream(authorities).anyMatch(s::equals);
    }

    @Override
    public boolean isSecure() {
        return delegate.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return delegate.getAuthenticationScheme();
    }
}
