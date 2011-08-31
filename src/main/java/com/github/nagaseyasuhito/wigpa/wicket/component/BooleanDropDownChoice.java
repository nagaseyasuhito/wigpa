package com.github.nagaseyasuhito.wigpa.wicket.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.github.nagaseyasuhito.wigpa.wicket.util.BooleanChoiceRenderer;

public class BooleanDropDownChoice extends DropDownChoice<Boolean> {
	private static final long serialVersionUID = 1L;

	public BooleanDropDownChoice(String id, IModel<Boolean> model, String prefix) {
		super(id, model, (List<Boolean>) null);

		this.setChoices(Arrays.asList(true, false));
		this.setChoiceRenderer(new BooleanChoiceRenderer(prefix, this));
	}

	public BooleanDropDownChoice(String id, String prefix) {
		this(id, null, prefix);
	}
}
