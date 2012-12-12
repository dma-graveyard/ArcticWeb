package dk.dma.arcticweb.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dk.dma.arcticweb.dao.StakeholderDao;
import dk.dma.arcticweb.dao.UserDao;
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
	
}
