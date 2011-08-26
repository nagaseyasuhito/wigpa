package com.github.nagaseyasuhito.wigpa.wicket.component;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractConverter;

public class AbbreviateLabel extends Label {
	private static final long serialVersionUID = 1L;

	private int length;

	public AbbreviateLabel(String id, IModel<?> model, int length) {
		super(id, model);
		this.length = length;
	}

	public AbbreviateLabel(String id, int length) {
		super(id);
		this.length = length;
	}

	public AbbreviateLabel(String id, String label, int length) {
		super(id, label);
		this.length = length;
	}

	@Override
	public <C> IConverter<C> getConverter(final Class<C> type) {
		return new AbstractConverter<C>() {
			private static final long serialVersionUID = 1L;

			@Override
			public C convertToObject(String value, Locale locale) {
				return null;
			}

			@Override
			public String convertToString(C value, Locale locale) {
				String string = AbbreviateLabel.super.getConverter(type).convertToString(value, locale);

				return StringUtils.abbreviate(string, AbbreviateLabel.this.length);
			}

			@Override
			protected Class<C> getTargetType() {
				return type;
			}
		};
	}
}
