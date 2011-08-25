package jp.wigpa.wicket.component;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class TimeField extends AutoCompleteTextField<Time> {
	private static final IAutoCompleteRenderer<Time> AUTO_COMPLETE_TEXT_RENDERER = new AbstractAutoCompleteTextRenderer<Time>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected String getTextValue(Time object) {
			return Application.get().getConverterLocator().getConverter(Time.class).convertToString(object, Session.get().getLocale());
		}
	};

	private static final Pattern NORMALIZE_PATTERN = Pattern.compile("^0|[^0-9]");;

	private static final long serialVersionUID = 1L;

	private static AutoCompleteSettings newDefaultAutoCompleteSettings() {
		AutoCompleteSettings settings = new AutoCompleteSettings();
		settings.setShowCompleteListOnFocusGain(true);
		settings.setShowListOnEmptyInput(true);
		settings.setShowListOnFocusGain(true);
		settings.setMaxHeightInPx(160);
		settings.setThrottleDelay(0);

		return settings;
	}

	private final List<Time> times;

	public TimeField(String id) {
		this(id, null, TimeField.newDefaultAutoCompleteSettings());
	}

	public TimeField(String id, AutoCompleteSettings settings) {
		this(id, null, settings);
	}

	public TimeField(String id, IModel<Time> object) {
		this(id, object, TimeField.newDefaultAutoCompleteSettings());
	}

	public TimeField(String id, IModel<Time> object, AutoCompleteSettings settings) {
		super(id, object, null, TimeField.AUTO_COMPLETE_TEXT_RENDERER, settings);

		this.setType(Time.class);
		this.add(new AttributeAppender("class", Model.of("time"), " "));

		Calendar calendar = Calendar.getInstance(Session.get().getLocale());
		calendar.clear();
		List<Time> times = new ArrayList<Time>();

		for (int i = 0; i < 48; i++) {
			times.add(new Time(calendar.getTimeInMillis()));
			calendar.add(Calendar.MINUTE, 30);
		}

		this.times = Collections.unmodifiableList(times);

	}

	@Override
	protected Iterator<Time> getChoices(final String input) {
		final String normalizedInput = TimeField.NORMALIZE_PATTERN.matcher(input).replaceAll("");

		return Collections2.filter(this.times, new Predicate<Time>() {

			@Override
			public boolean apply(Time time) {
				String timeAsString = TimeField.this.getConverter(Time.class).convertToString(time, TimeField.this.getLocale());
				return TimeField.NORMALIZE_PATTERN.matcher(timeAsString).replaceAll("").startsWith(normalizedInput);
			}
		}).iterator();
	}
}
