package dk.dma.arcticweb.service;

import javax.ejb.Local;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.ShipReport;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;

@Local
public interface StakeholderService {
	
	/**
	 * Get stakeholder given user
	 * @param user
	 * @return
	 */
	Stakeholder getStakeholder(User user);
	
	/**
	 * Persist stakeholder
	 * @param stakeholder
	 * @return
	 */
	Stakeholder save(Stakeholder stakeholder);

	/**
	 * Add ship report for ship
	 * @param ship
	 * @param shipReport
	 */
	void addShipReport(Ship ship, ShipReport shipReport);
	
}
