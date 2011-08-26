package com.github.nagaseyasuhito.wigpa.wicket.component;

import java.util.Date;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class DateField extends TextField<Date> {
	private static final long serialVersionUID = 1L;

	public DateField(String id) {
		this(id, null);
	}

	public DateField(String id, IModel<Date> model) {
		super(id, model);

		this.setType(Date.class);
		this.add(new AttributeAppender("class", Model.of("date"), " "));
	}
}
