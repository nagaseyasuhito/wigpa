package jp.wigpa.wicket.converter;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.util.convert.converter.AbstractConverter;

public class EnumerationConverter<T extends Enum<T>> extends AbstractConverter<T> {
	private static final long serialVersionUID = 1L;

	private Class<T> clazz;

	public EnumerationConverter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T convertToObject(String value, Locale locale) {
		throw new IllegalAccessError();
	}

	@Override
	public String convertToString(T value, Locale locale) {
		if (value == null) {
			return null;
		}

		return Application.get().getResourceSettings().getLocalizer().getString(value.getClass().getName() + "." + value, (Component) null);
	}

	@Override
	protected Class<T> getTargetType() {
		return this.clazz;
	}
}
