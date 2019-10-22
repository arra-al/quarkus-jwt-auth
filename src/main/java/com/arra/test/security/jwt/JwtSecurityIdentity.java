package com.arra.test.security.jwt;

import io.quarkus.security.credential.Credential;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.SecurityIdentity;

import java.security.Permission;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class JwtSecurityIdentity implements SecurityIdentity {
    public JwtPrincipal principal;
    public JwtSecurityIdentity(JwtPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAnonymous() {
        return principal == null;
    }

    @Override
    public Set<String> getRoles() {
        String[] roles = principal.getAuthorities();
        return Arrays.stream(roles).collect(Collectors.toSet());
    }

    @Override
    public <T extends Credential> T getCredential(Class<T> aClass) {
        return new TokenCredential(null, "jwt");
    }

    @Override
    public Set<Credential> getCredentials() {
        return null;
    }

    @Override
    public <T> T getAttribute(String s) {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public CompletionStage<Boolean> checkPermission(Permission permission) {
        return null;
    }
}
