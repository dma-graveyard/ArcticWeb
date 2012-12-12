package dk.dma.arcticweb.site.pages.main.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.site.pages.main.MainPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

public class ShipInformationForm extends Form<ShipInformationForm> {
	
	private static final long serialVersionUID = 1L;
	
	private TextField<Long> mmsi;
	private TextField<String> name;
	
	private FeedbackPanel feedback;
	private AjaxSubmitLink saveLink;

	public ShipInformationForm(String id) {
		super(id);
		
		Ship ship = (Ship)ArcticWebSession.get().getStakeholder();
		
		setDefaultModel(new CompoundPropertyModel<Ship>(ship));
		
		mmsi = new TextField<Long>("mmsi");
		mmsi.setRequired(true);
		name = new TextField<String>("name");
		name.setRequired(true);
		
		
		
		feedback = new FeedbackPanel("ship_information_feedback");
		feedback.setVisible(false);
		
		saveLink = new AjaxSubmitLink("save") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO How to work with JPA entities
				
				setResponsePage(new MainPage());
				
			}
		};
		
		add(mmsi);
		add(name);
		add(feedback);
		add(saveLink);
		
	}

}
