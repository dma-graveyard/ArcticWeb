package dk.dma.arcticweb.service;

import com.google.inject.ImplementedBy;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;

@ImplementedBy(StakeholderServiceImpl.class)
public interface StakeholderService {
	
	/**
	 * Get stakeholder given user
	 * @param user
	 * @return
	 */
	Stakeholder getStakeholder(User user);
	
	/**
	 * Get ship for user
	 * @param user
	 * @return
	 */
	Ship getShip(User user);
	
	
}
