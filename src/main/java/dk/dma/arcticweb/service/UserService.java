package dk.dma.arcticweb.service;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.User;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
	
	User createUser(User user);

}
