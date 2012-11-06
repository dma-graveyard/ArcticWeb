package dk.dma.arcticweb.ejb.eao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import dk.dma.arcticweb.ejb.domain.User;

@Stateless
public class UserEao extends EaoImpl {
	
	/**
	 * Get user by username
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public User getByUsername(String username) {
		Query query = em.createNamedQuery("User:getByUsername");
		query.setParameter("username", username);
		return (User)getSingleOrNull(query.getResultList());
	}
	
}
