package com.arra.test;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AppLifecycleBean {
    private static final Logger log = LoggerFactory.getLogger("AppLifecycleBean");

    public void onStart(@Observes StartupEvent ev) {
        log.info("The app is starting ...");
    }

    public void onStop(@Observes ShutdownEvent ev) {
        log.info("The app is stopping ...");
    }
}
