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
package dk.dma.arcticweb.site.pages.main;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

import dk.dma.arcticweb.site.SecurePage;
import dk.dma.arcticweb.site.pages.BasePage;
import dk.dma.arcticweb.site.pages.front.FrontPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

public class MainPage extends BasePage implements SecurePage {
	private static final long serialVersionUID = 1L;
	
	public MainPage() {
		super();
		
		add(new Label("username", new PropertyModel<ArcticWebSession>(this, "username")));
		
		Link<String> logoutLink = new Link<String>("logout_link") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				ArcticWebSession.get().logout();
				setResponsePage(FrontPage.class);
			}
		};
		add(logoutLink);
		
	}
		

}
