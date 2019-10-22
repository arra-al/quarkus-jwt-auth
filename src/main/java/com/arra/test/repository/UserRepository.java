package com.arra.test.repository;

import com.arra.test.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    public User findByUsername(String username) {
        log.info("querying user by username: {}", username);
        User u = find("username", username).firstResult();
        return u;
    }
}
