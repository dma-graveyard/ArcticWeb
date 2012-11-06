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

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

import dk.dma.arcticweb.domain.User;
import dk.dma.arcticweb.service.UserService;

public class FrontPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	@Inject
	UserService userService;

	public FrontPage(final PageParameters parameters) {
		super(parameters);
		
		User user = userService.getByUsername("ole");
		System.out.println("user: "+ user);

	}
}
