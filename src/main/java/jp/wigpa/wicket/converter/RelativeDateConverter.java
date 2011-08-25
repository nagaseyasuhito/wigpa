package jp.wigpa.wicket.converter;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.converter.AbstractConverter;

public class RelativeDateConverter extends AbstractConverter<Date> {
	private static final long serialVersionUID = 1L;

	@Override
	public Date convertToObject(String value, Locale locale) {
		throw new IllegalAccessError();
	}

	@Override
	public String convertToString(Date value, Locale locale) {
		long duration = (System.currentTimeMillis() - value.getTime()) / 1000;

		Localizer localizer = Application.get().getResourceSettings().getLocalizer();

		if (duration < 60) {
			return localizer.getString("RelativeDateConverter.second", null, Model.of(duration));
		}

		duration /= 60;
		if (duration < 60) {
			return localizer.getString("RelativeDateConverter.minute", null, Model.of(duration));
		}

		duration /= 24;
		if (duration < 24) {
			return localizer.getString("RelativeDateConverter.hour", null, Model.of(duration));
		}

		duration /= 7;
		if (duration == 1) {
			return localizer.getString("RelativeDateConverter.yesterday", null, Model.of(duration));
		}
		if (duration < 7) {
			return localizer.getString("RelativeDateConverter.day", null, Model.of(duration));
		}

		return Application.get().getConverterLocator().getConverter(Date.class).convertToString(value, locale);
	}

	@Override
	protected Class<Date> getTargetType() {
		return Date.class;
	}

}
