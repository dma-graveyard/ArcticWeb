package dk.dma.arcticweb.site.pages.main.panel;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import dk.dma.arcticweb.site.pages.front.FrontPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

public class UserPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(UserPanel.class);

	public UserPanel(String id) {
		super(id);
				
		WebMarkupContainer notLoggedIn = new WebMarkupContainer("not_logged_in") {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isVisible() {
				return !ArcticWebSession.get().isLoggedIn();
			}
		};
		add(notLoggedIn);
		WebMarkupContainer loggedIn = new WebMarkupContainer("logged_in") {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isVisible() {
				return ArcticWebSession.get().isLoggedIn();
			}
		};
		add(loggedIn);
		
		loggedIn.add(new Label("username", new PropertyModel<ArcticWebSession>(this, "username")));
		
		Link<String> logoutLink = new Link<String>("logout_link") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				LOG.info("Logging out: " + getUsername());
				ArcticWebSession.get().logout();
				setResponsePage(FrontPage.class);
			}
		};
		loggedIn.add(logoutLink);
		
	}
	
	public String getUsername() {		
		if (ArcticWebSession.get().isLoggedIn()) {
			return ArcticWebSession.get().getUser().getUsername();
		}
		return null;
	}

}
