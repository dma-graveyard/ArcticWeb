package se.lil;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {
    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, new MyServletModule()));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
