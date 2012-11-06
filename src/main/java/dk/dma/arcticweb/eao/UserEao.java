package dk.dma.arcticweb.eao;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.User;

@ImplementedBy(UserEaoImpl.class)
public interface UserEao extends Eao {
	
	User getByUsername(String username);
	
	boolean userExists(String username);

}
