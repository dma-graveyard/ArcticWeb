package dk.dma.arcticweb.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dk.dma.arcticweb.dao.StakeholderDao;
import dk.dma.arcticweb.dao.UserDao;
import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.ShipReport;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;

@Stateless
public class StakeholderServiceImpl implements StakeholderService {
	
	@EJB
	StakeholderDao stakeholderDao;
	@EJB
	UserDao userDao;
	
	@Override
	public Stakeholder getStakeholder(User user) {
		user = (User)userDao.getByPrimaryKey(User.class, user.getId());
		Stakeholder stakeholder = user.getStakeholder();
		stakeholder = stakeholderDao.get(stakeholder.getId());
		return stakeholder;
	}
	
	@Override
	public Stakeholder save(Stakeholder stakeholder) {
		return (Stakeholder) stakeholderDao.saveEntity(stakeholder);
	}
	
	@Override
	public void addShipReport(Ship ship, ShipReport shipReport) {
		shipReport.setCreated(new Date());		
		ship = (Ship)stakeholderDao.get(ship.getId());
		shipReport.setShip(ship);
		stakeholderDao.saveEntity(shipReport);
	}
	
}
