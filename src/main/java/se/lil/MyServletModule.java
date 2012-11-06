package se.lil;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

public class MyServletModule extends ServletModule {
    protected void configureServlets() {
        install(new JpaPersistModule("manager1"));
        filter("/*").through(PersistFilter.class);
    }
}
