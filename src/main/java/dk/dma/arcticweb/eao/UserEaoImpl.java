package dk.dma.arcticweb.eao;

import javax.persistence.Query;

import com.google.inject.persist.Transactional;

import dk.dma.arcticweb.domain.User;

@Transactional
public class UserEaoImpl extends EaoImpl implements UserEao {
	
	public UserEaoImpl() {
		System.out.println("Creating user eao impl");
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public User getByUsername(String username) {
		Query query = em.createNamedQuery("User:getByUsername");
		query.setParameter("username", username);
		return (User) getSingleOrNull(query.getResultList());
	}
		
	@Override
	public boolean userExists(String username) {
		return (getByUsername(username) != null);
	}

}
