package dk.dma.arcticweb.site;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

@Singleton
public class JpaInitializer {
	
	@Inject
    public JpaInitializer(final PersistService service) {
        service.start();
    }

}
