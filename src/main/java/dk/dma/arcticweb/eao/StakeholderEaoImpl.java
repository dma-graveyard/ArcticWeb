package dk.dma.arcticweb.eao;

import java.util.List;

import javax.persistence.Query;

import dk.dma.arcticweb.domain.Stakeholder;

public class StakeholderEaoImpl extends EaoImpl implements StakeholderEao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Stakeholder> getAll() {
		Query query = em.createQuery("SELECT s FROM Stakeholder s");
		return query.getResultList();
	}

	

}
