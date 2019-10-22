package com.arra.test.web.vm;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbProperty;

/**
 * Object to return as body in JWT Authentication.
 */
@RegisterForReflection
public class JWTTokenVM {
    private String idToken;

    public JWTTokenVM(String idToken) {
        this.idToken = idToken;
    }

    @JsonbProperty("access_token")
    String getIdToken() {
        return idToken;
    }

    void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
