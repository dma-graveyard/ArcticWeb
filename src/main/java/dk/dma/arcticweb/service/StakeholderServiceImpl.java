package dk.dma.arcticweb.service;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.StakeholderEao;
import dk.dma.arcticweb.eao.UserEao;

public class StakeholderServiceImpl implements StakeholderService {
	
	@Inject
	StakeholderEao stakeholderEao;
	@Inject
	UserEao userEao;
	
	@Override
	public Stakeholder getStakeholder(User user) {
		user = (User)userEao.getByPrimaryKey(User.class, user.getId());
		System.out.println("class: " + user.getClass());
		Stakeholder stakeholder = user.getStakeholder();
		System.out.println("class: " + stakeholder.getClass());
		
		stakeholder = (Stakeholder)stakeholderEao.getByPrimaryKey(Stakeholder.class, stakeholder.getId());
		System.out.println("class: " + stakeholder.getClass());
		
		return stakeholder;
	}
	
	@Override
	public Ship getShip(User user) {
		user = (User)userEao.getByPrimaryKey(User.class, user.getId());
		System.out.println("class: " + user.getClass());
		Stakeholder stakeholder = user.getStakeholder();
		if (stakeholder == null) {
			return null;
		}		
		return stakeholderEao.getShip(stakeholder.getId());
	}

}
