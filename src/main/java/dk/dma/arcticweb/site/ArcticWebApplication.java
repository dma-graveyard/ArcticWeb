/* Copyright (c) 2012 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.arcticweb.site;

import org.apache.wicket.Session;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import dk.dma.arcticweb.site.pages.front.FrontPage;
import dk.dma.arcticweb.site.pages.main.MainPage;
import dk.dma.arcticweb.site.pages.test.TestPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

/**
 * Application object for web application
 */
public class ArcticWebApplication extends WebApplication {

	@Override
	public Class<? extends WebPage> getHomePage() {
		return FrontPage.class;
	}

	@Override
	public void init() {
		super.init();
		getComponentInstantiationListeners().add(new GuiceComponentInjector(this, new ArcticServletModule()));
		
		// Set security
		getSecuritySettings().setAuthorizationStrategy(new AuthStrategy());
		
		// Mount pages
		mountPage("/main", MainPage.class);
		mountPage("/front", FrontPage.class);
		mountPage("/test", TestPage.class);
	}
	
	@Override
	public Session newSession(Request request, Response response) {		
		return new ArcticWebSession(request);
	}
	
}
