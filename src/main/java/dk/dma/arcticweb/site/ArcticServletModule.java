package dk.dma.arcticweb.site;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

public class ArcticServletModule extends ServletModule {
    protected void configureServlets() {
        install(new JpaPersistModule("arcticweb"));
        filter("/*").through(PersistFilter.class);
    }
}
