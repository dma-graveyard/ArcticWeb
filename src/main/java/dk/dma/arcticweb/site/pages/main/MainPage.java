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
package dk.dma.arcticweb.site.pages.main;

import javax.ejb.EJB;

import org.apache.wicket.markup.html.basic.Label;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.domain.Stakeholder;
import dk.dma.arcticweb.service.StakeholderService;
import dk.dma.arcticweb.site.SecurePage;
import dk.dma.arcticweb.site.pages.BasePage;
import dk.dma.arcticweb.site.pages.main.panel.MapPanel;
import dk.dma.arcticweb.site.pages.main.panel.UserPanel;

public class MainPage extends BasePage implements SecurePage {
	private static final long serialVersionUID = 1L;
	
	@EJB
	StakeholderService stakeholderService;
	
	public MainPage() {
		super();		
		add(new UserPanel("user_panel"));
		add(new MapPanel("map"));
		
		// TODO Move the below thing to a special component
		// Make genric JS component with methods to add vars and values
		// Use type of value to determine how to represent
		// Get stakeholder		
		StringBuilder js = new StringBuilder();
		Stakeholder stakeholder = stakeholderService.getStakeholder(getUser());
		System.out.println("stakeholder: " + stakeholder);
		System.out.println("stakholder.class: " + stakeholder.getClass());
		String stakeholderType = ((stakeholder != null) ? stakeholder.getClass().getSimpleName() : "");
		
		String shipMmsi = "null";
		Ship ship = stakeholderService.getShip(getUser());
		if (ship != null) {
			shipMmsi = Integer.toString(ship.getMmsi());
		}
				
		js.append("var stakeholder_type = '" + stakeholderType + "';\n");
		js.append("var ship_mmsi = " + shipMmsi + ";\n");
		add(new Label("js", "\n" + js.toString()).setEscapeModelStrings(false));
		
		
		
	}
		

}
