package dk.dma.arcticweb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;

@Stateless
public class StakeholderDaoImpl extends DaoImpl implements StakeholderDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Stakeholder> getAll() {
		Query query = em.createQuery("SELECT s FROM Stakeholder s");
		return query.getResultList();
	}

	@Override
	public Ship getShip(int id) {
		return em.find(Ship.class, id);
	}
	

}
