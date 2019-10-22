package com.arra.test.web.rest;

import com.arra.test.domain.User;
import com.arra.test.repository.UserRepository;
import com.arra.test.security.jwt.JwtPrincipal;
import com.arra.test.security.jwt.TokenProvider;
import com.arra.test.web.filter.JwtFilter;
import com.arra.test.web.vm.JWTTokenVM;
import com.arra.test.web.vm.UserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    private UserRepository repository;

    @Inject
    private TokenProvider tokenProvider;

    @POST
    @Path("/login")
    public Response login(UserVM credentials, @Context SecurityContext ctx) {
//        if(ctx.getUserPrincipal().getName() == credentials.username) {
//            return Response.status(304).build();
//        }
        User u = repository.findByUsername(credentials.username);
        log.info("credentials: {} {}", credentials.username, credentials.password);
        log.info("user from rep: {}", u);
        if(u == null) {
            return Response.noContent().status(401).build();
        }
        // passwords need to be encrypted with BCrypt
        if (credentials.password.equals(u.password)) {
            // generate jwt
            String jwt = tokenProvider.createToken(new JwtPrincipal(u.username), credentials.rememberMe);
            log.info("created token: {}", jwt);
            return Response.ok(new JWTTokenVM(jwt)).status(200).header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt).build();
        }
        log.info("empty token: {} ", u.password, credentials.password);
        return Response.ok(new JWTTokenVM("no token")).status(404).build();
    }
}