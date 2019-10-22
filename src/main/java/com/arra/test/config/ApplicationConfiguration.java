package com.arra.test.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "application")
public class ApplicationConfiguration {

    public JwtConfiguration jwtConfiguration = new JwtConfiguration();

    @ConfigProperties(prefix = "jwtConfiguration")
    public static class JwtConfiguration {
        @ConfigProperty(name = "base64Secret")
        public String base64Secret;

        @ConfigProperty(name = "tokenValidityInSeconds", defaultValue = "300")
        public long tokenValidityInSeconds;

        @ConfigProperty(name = "tokenValidityInSecondsForRememberMe", defaultValue = "7200")
        public long tokenValidityInSecondsForRememberMe;
    }
}
