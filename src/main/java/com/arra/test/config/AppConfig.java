package com.arra.test.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "application")
public class AppConfig {
    @ConfigProperty(name = "security.authentication.jwt.base64-secret")
    public String jwtSecret;

    @ConfigProperty(name = "security.authentication.jwt.token-validity-in-seconds", defaultValue = "300")
    public long jwtValidityInSec;

    @ConfigProperty(name = "security.authentication.jwt.token-validity-in-seconds-for-remember-me", defaultValue = "7200")
    public long jwtRememberMeSec;

}
