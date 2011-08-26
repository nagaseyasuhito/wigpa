package com.github.nagaseyasuhito.wigpa.wicket.converter;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.util.convert.converter.AbstractConverter;

public class BooleanConverter extends AbstractConverter<Boolean> {
	private static final long serialVersionUID = 1L;

	private String prefix;

	public BooleanConverter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Boolean convertToObject(String value, Locale locale) {
		throw new IllegalAccessError();
	}

	@Override
	public String convertToString(Boolean value, Locale locale) {
		if (value == null) {
			return null;
		}

		return Application.get().getResourceSettings().getLocalizer().getString(this.prefix + "." + value, (Component) null, (String) null);
	}

	@Override
	protected Class<Boolean> getTargetType() {
		return Boolean.class;
	}
}
