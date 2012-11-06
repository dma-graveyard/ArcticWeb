package dk.dma.arcticweb.service;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.User;

@ImplementedBy(UserServiceImpl.class)
public interface UserService extends IService {
	
	User getByUsername(String username);
	

}
