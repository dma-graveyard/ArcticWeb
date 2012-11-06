package dk.dma.arcticweb.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.UserEao;

@Transactional
public class UserServiceImpl implements UserService {
	
	@Inject
	private UserEao userEao;
	
	@Override
	public User createUser(User user) {
		return (User)userEao.saveEntity(user);
	}
	
}
