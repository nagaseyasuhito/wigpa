package jp.wigpa.wicket.component;

import java.util.Arrays;
import java.util.List;

import jp.wigpa.wicket.util.BooleanChoiceRenderer;

import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;

public class BooleanRadioChoice extends RadioChoice<Boolean> {
	private static final long serialVersionUID = 1L;

	public BooleanRadioChoice(String id, IModel<Boolean> model, String prefix) {
		super(id, model, (List<Boolean>) null);

		this.setChoices(Arrays.asList(true, false));
		this.setChoiceRenderer(new BooleanChoiceRenderer(prefix, this));
	}

	public BooleanRadioChoice(String id, String prefix) {
		this(id, null, prefix);
	}
}
