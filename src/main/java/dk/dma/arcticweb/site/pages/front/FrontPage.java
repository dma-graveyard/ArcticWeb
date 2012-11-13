/* Copyright (c) 2012 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.arcticweb.site.pages.front;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.StakeholderEao;
import dk.dma.arcticweb.eao.UserEao;
import dk.dma.arcticweb.site.pages.BasePage;

public class FrontPage extends BasePage {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserEao userEao;
	@Inject
	private StakeholderEao stakeholderEao;

	public FrontPage() {
		super();
		
		User user = userEao.getByUsername("obo");
		System.out.println("user: "+ user);
		
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
