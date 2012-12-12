package dk.dma.arcticweb.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dk.dma.arcticweb.dao.StakeholderDao;
import dk.dma.arcticweb.dao.UserDao;
import dk.dma.arcticweb.domain.Ship;
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
		System.out.println("class: " + user.getClass());
		Stakeholder stakeholder = user.getStakeholder();
		System.out.println("class: " + stakeholder.getClass());
		
		stakeholder = (Stakeholder)stakeholderDao.getByPrimaryKey(Stakeholder.class, stakeholder.getId());
		System.out.println("class: " + stakeholder.getClass());
		
		return stakeholder;
	}
	
	@Override
	public Ship getShip(User user) {
		user = (User)userDao.getByPrimaryKey(User.class, user.getId());
		System.out.println("class: " + user.getClass());
		Stakeholder stakeholder = user.getStakeholder();
		if (stakeholder == null) {
			return null;
		}		
		return stakeholderDao.getShip(stakeholder.getId());
	}

}
