package dk.dma.arcticweb.site.pages.front;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.service.UserService;
import dk.dma.arcticweb.site.pages.main.MainPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

public class LoginForm extends StatelessForm<LoginForm> {
	
	private static final Logger LOG = Logger.getLogger(LoginForm.class);

	@Inject
	UserService userService;

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private Boolean rememberMe;

	public LoginForm(String id) {
		super(id);
		setModel(new CompoundPropertyModel<LoginForm>(this));
		add(new TextField<String>("username").setRequired(true));
		add(new PasswordTextField("password").setRequired(true));
	}

	@Override
	protected final void onSubmit() {
		LOG.info("Trying to login: " + username);
		User user = userService.login(username, password);
		if (user != null) {
			LOG.info("User logged in: " + username);
			ArcticWebSession.get().setUser(user);
			setResponsePage(new MainPage());
		} else {
			error("Wrong username or password");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
