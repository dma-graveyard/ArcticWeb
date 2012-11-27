package dk.dma.arcticweb.site;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

import dk.dma.arcticweb.eao.Eao;
import dk.dma.arcticweb.eao.EaoImpl;

public class JpaModule extends ServletModule {

	@Override
	protected void configureServlets() {
		install(new JpaPersistModule("jpaUnit"));
		filter("/*").through(PersistFilter.class);
		bind(JpaInitializer.class).asEagerSingleton();
		// dao stuff
		bind(Eao.class).to(EaoImpl.class);
	}

}
