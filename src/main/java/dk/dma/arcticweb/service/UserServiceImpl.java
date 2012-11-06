package dk.dma.arcticweb.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.persist.Transactional;

import dk.dma.arcticweb.domain.User;

@Transactional
public class UserServiceImpl extends ServiceImpl implements UserService {
	
	@Inject
    private EntityManager em;

	/**
	 * Get user by username
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User getByUsername(String username) {
		Query query = em.createNamedQuery("User:getByUsername");
		query.setParameter("username", username);
		return (User) getSingleOrNull(query.getResultList());
	}

}
