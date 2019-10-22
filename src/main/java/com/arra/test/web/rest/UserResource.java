package com.arra.test.web.rest;

import com.arra.test.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Path("/api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static Logger log = LoggerFactory.getLogger(UserResource.class);
    @GET
    @RolesAllowed("*")
    public List<User> getAll(@Context SecurityContext sec) {
        Principal user = sec.getUserPrincipal();
        String name = user != null ? user.getName() : "anonymous";
        log.info("auth user: {}", name);
        return User.listAll();
    }

    @POST
    @Transactional
    public Response add(User user) {
        user.persist();
        return Response.ok(user).status(201).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        User u  = User.findById(id);
        u.delete();
        return Response.status(204).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response update(User user, @PathParam("id") UUID id) {
        User u  = User.findById(id);
        u.username = user.username;
        u.email = user.email;;
        //u.persist();
        return Response.ok(u).status(200).build();
    }
}
