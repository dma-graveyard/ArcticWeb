package dk.dma.arcticweb.site.pages.test;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebPage;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.StakeholderEao;
import dk.dma.arcticweb.eao.UserEao;

public class TestPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private StakeholderEao stakeholderEao;
	@Inject
	private UserEao userEao;

	public TestPage() {
		Ship newShip = new Ship();
		newShip.setName("ORASILA");
		newShip.setMmsi(220443000);
				
		
		User user = new User();
		user.setUsername("testuser");
		user.setPassword("testuser"); 
		user.setEmail("obo@dma.dk");
		user.setStakeholder(newShip);
		
		newShip.getUsers().add(user);
		
		stakeholderEao.saveEntity(newShip);
		userEao.saveEntity(user);
		
		
		List<Stakeholder> stakeholders = stakeholderEao.getAll();
		for (Stakeholder stakeholder : stakeholders) {
			if (stakeholder instanceof Ship) {
				Ship ship = (Ship)stakeholder;
				System.out.println("mmsi: " + ship.getMmsi());
				Set<User> users = ship.getUsers();
				System.out.println("users: " + users.size());
			}
		}		
		
	}

}
