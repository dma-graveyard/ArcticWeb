package dk.dma.arcticweb.service;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.User;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
	
	/**
	 * Get user given username and password. Null if invalid
	 * username or password.
	 * @param username
	 * @param password
	 * @return
	 */
	User login(String username, String password);
	
	/**
	 * Create a new user
	 * @param user
	 * @return
	 */
	User createUser(User user);
	

}
