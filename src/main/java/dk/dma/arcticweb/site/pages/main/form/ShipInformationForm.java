package dk.dma.arcticweb.site.pages.main.form;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;

import dk.dma.arcticweb.domain.Ship;
import dk.dma.arcticweb.service.StakeholderService;
import dk.dma.arcticweb.site.pages.main.MainPage;
import dk.dma.arcticweb.site.session.ArcticWebSession;

public class ShipInformationForm extends Form<ShipInformationForm> {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	StakeholderService stakeholderService;
	
	private TextField<Long> mmsi;
	private TextField<String> name;
	
	private FeedbackPanel feedback;
	private AjaxSubmitLink saveLink;
	private Link<ShipInformationForm> closeLink;
	private WebMarkupContainer saved;

	public ShipInformationForm(String id) {
		super(id);		
		final Ship ship = (Ship)stakeholderService.getStakeholder(ArcticWebSession.get().getUser());		
		setDefaultModel(new CompoundPropertyModel<Ship>(ship));
		
		mmsi = new TextField<Long>("mmsi");
		mmsi.setType(Long.class);
		mmsi.setRequired(true).add(new MinimumValidator<Long>(100000000L)).add(new MaximumValidator<Long>(999999999L));
		
		name = new TextField<String>("name");
		name.setRequired(true);
		
		feedback = new FeedbackPanel("ship_information_feedback");
		feedback.setVisible(false);
		saved = new WebMarkupContainer("saved");
		saved.setVisible(false);
		closeLink = new Link<ShipInformationForm>("close") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				feedback.setVisible(false);
				saved.setVisible(false);
				setResponsePage(new MainPage());
			}
		};
		
		saveLink = new AjaxSubmitLink("save") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				stakeholderService.save(ship);
				ArcticWebSession.get().refresh();
				feedback.setVisible(false);
				saved.setVisible(true);
				target.add(this.getParent());				
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				saved.setVisible(false);
				feedback.setVisible(true);
				target.add(this.getParent());
			}
		};
		
		add(mmsi);
		add(name);
		add(feedback);
		add(saveLink);
		add(saved);
		add(closeLink);
		
	}

}
