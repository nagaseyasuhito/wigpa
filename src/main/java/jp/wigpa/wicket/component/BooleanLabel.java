package jp.wigpa.wicket.component;

import jp.wigpa.wicket.converter.BooleanConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class BooleanLabel extends Label {
	private static final long serialVersionUID = 1L;

	private String prefix;

	public BooleanLabel(String id) {
		this(id, id);
	}

	public BooleanLabel(String id, IModel<?> model) {
		this(id, model, id);
	}

	public BooleanLabel(String id, IModel<?> model, String prefix) {
		super(id, model);
		this.prefix = prefix;
	}

	public BooleanLabel(String id, String prefix) {
		this(id, null, prefix);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		return (IConverter<C>) new BooleanConverter(this.prefix);
	}
}
