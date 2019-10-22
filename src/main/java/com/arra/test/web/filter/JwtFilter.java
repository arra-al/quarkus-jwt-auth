package com.arra.test.web.filter;

import com.arra.test.security.jwt.JWTSecurityContext;
import com.arra.test.security.jwt.JwtPrincipal;
import com.arra.test.security.jwt.TokenProvider;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Provider
public class JwtFilter implements ContainerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_TOKEN = "access_token";

    @Inject
    private TokenProvider tokenProvider;

    @Context
    HttpServerRequest request;

    @Context
    SecurityContext ctx;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String jwt = this.resolveToken(request);
        log.info("validating jwt token: {}", jwt);
        if(tokenProvider.validateToken(jwt)) {
            Principal principal = this.tokenProvider.getPrincipal(jwt);
            containerRequestContext.setSecurityContext(new JWTSecurityContext(ctx, (JwtPrincipal) principal));
        }
    }

    private String resolveToken(HttpServerRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParam(AUTHORIZATION_TOKEN);
        if (jwt != null && !"".equals(jwt.trim())) {
            return jwt;
        }
        return null;
    }
}
