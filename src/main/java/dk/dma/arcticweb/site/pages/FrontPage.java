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
package dk.dma.arcticweb.site.pages;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.eao.StakeholderEao;
import dk.dma.arcticweb.eao.UserEao;

public class FrontPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserEao userEao;
	@Inject
	private StakeholderEao stakeholderEao;

	public FrontPage(final PageParameters parameters) {
		super(parameters);
		
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
