package dk.dma.arcticweb.site;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for web application
 */
public class ArcticWebApplication extends WebApplication
{    	

	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return FrontPage.class;
	}

	@Override
	public void init()
	{
		super.init();

		// add your configuration here
	}
}
