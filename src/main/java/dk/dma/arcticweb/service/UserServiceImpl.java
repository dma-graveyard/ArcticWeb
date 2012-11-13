package dk.dma.arcticweb.service;

import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.UserEao;

@Transactional
public class UserServiceImpl implements UserService {
	
	@Inject
	private UserEao userEao;
	
	@Override
	public User login(String username, String password) {
		User user = userEao.getByUsername(username);
		if (user == null) {
			return null;
		}
		if (!user.passwordMatch(password)) {
			return null;
		}
		user.setLastLogin(new Date());
		return (User)userEao.saveEntity(user);
	}
	
	@Override
	public User createUser(User user) {
		return (User)userEao.saveEntity(user);
	}
	
}
