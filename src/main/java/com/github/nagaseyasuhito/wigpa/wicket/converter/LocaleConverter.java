package com.github.nagaseyasuhito.wigpa.wicket.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.converter.AbstractConverter;

public class LocaleConverter extends AbstractConverter<Locale> {
	private static final long serialVersionUID = 1L;

	@Override
	public Locale convertToObject(String value, Locale locale) {
		throw new IllegalAccessError();
	}

	@Override
	public String convertToString(Locale value, Locale locale) {
		return value.getDisplayLanguage(locale) + " (" + value.getDisplayLanguage(value) + ")";
	}

	@Override
	protected Class<Locale> getTargetType() {
		return Locale.class;
	}
}
