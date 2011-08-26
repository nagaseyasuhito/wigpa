package com.github.nagaseyasuhito.wigpa.wicket.converter;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.apache.wicket.util.lang.PropertyResolver;

public class PropertyConverter<T> extends AbstractConverter<T> {
	private static final long serialVersionUID = 1L;

	private Class<T> clazz;

	private String expression;

	public PropertyConverter(Class<T> clazz, String expression) {
		this.clazz = clazz;
		this.expression = expression;
	}

	@Override
	public T convertToObject(String value, Locale locale) {
		throw new IllegalAccessError();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertToString(Object value, Locale locale) {
		if (value == null) {
			return null;
		}

		Object convertedValue = PropertyResolver.getValue(this.expression, value);
		IConverter<Object> converter = (IConverter<Object>) Application.get().getConverterLocator().getConverter(convertedValue.getClass());

		return value != null ? converter.convertToString(convertedValue, locale) : null;
	}

	@Override
	protected Class<T> getTargetType() {
		return this.clazz;
	}
}
