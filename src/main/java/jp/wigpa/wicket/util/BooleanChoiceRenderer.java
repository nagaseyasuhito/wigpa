package jp.wigpa.wicket.util;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class BooleanChoiceRenderer extends ChoiceRenderer<Boolean> {
	private static final long serialVersionUID = 1L;

	private Component component;

	private String prefix;

	public BooleanChoiceRenderer(String prefix, Component component) {
		this.prefix = prefix;
		this.component = component;
	}

	@Override
	public Object getDisplayValue(Boolean object) {
		return Application.get().getResourceSettings().getLocalizer().getString(this.prefix + "." + object.toString(), this.component);
	}
}