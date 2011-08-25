package jp.wigpa.wicket.component;

import jp.wigpa.wicket.converter.RelativeDateConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class RelativeDateLabel extends Label {
	private static final long serialVersionUID = 1L;

	public RelativeDateLabel(String id) {
		super(id);
	}

	public RelativeDateLabel(String id, IModel<?> model) {
		super(id, model);
	}

	public RelativeDateLabel(String id, String label) {
		super(id, label);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		return (IConverter<C>) new RelativeDateConverter();
	}
}