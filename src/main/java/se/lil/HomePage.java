package se.lil;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import se.lil.domain.User;
import se.lil.service.IService;

public class HomePage extends WebPage {
    private static final long serialVersionUID = -918138816287955837L;

    @Inject
    IService service;

    private final IModel<User> model = new LoadableDetachableModel<User>() {
        private static final long serialVersionUID = 1913317225318224531L;

        @Override
        protected User load() {
            return service.getUser();
        }
    };

    public HomePage() {
        setDefaultModel(new CompoundPropertyModel<>(model));
        add(new Label("name"));
        add(new Label("id"));
        add(new Label("description"));
    }
}
