package dk.dma.arcticweb.eao;

import java.util.List;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.Stakeholder;

@ImplementedBy(StakeholderEaoImpl.class)
public interface StakeholderEao extends Eao {
	
	/**
	 * Get all stakeholders
	 * @return
	 */
	List<Stakeholder> getAll();

}