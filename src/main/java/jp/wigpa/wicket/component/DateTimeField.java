package jp.wigpa.wicket.component;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DateTimeField extends FormComponentPanel<Date> {
	private static final long serialVersionUID = 1L;

	private DateField dateField;
	private TimeField timeField;

	public DateTimeField(String id) {
		this(id, null);
	}

	public DateTimeField(String id, IModel<Date> model) {
		super(id, model);

		this.dateField = new DateField("date", new PropertyModel<Date>(this, "modelObject"));
		this.timeField = new TimeField("time", new PropertyModel<Time>(this, "modelObject"));

		this.add(this.dateField);
		this.add(this.timeField);
	}

	@Override
	protected void convertInput() {
		if (this.dateField.getConvertedInput() == null || this.timeField.getConvertedInput() == null) {
			this.setConvertedInput(null);
			return;
		}

		Calendar date = Calendar.getInstance(Session.get().getLocale());
		date.setTime(this.dateField.getConvertedInput());

		Calendar time = Calendar.getInstance(Session.get().getLocale());
		time.setTime(this.timeField.getConvertedInput());

		Calendar calendar = Calendar.getInstance(Session.get().getLocale());
		calendar.clear();
		calendar.set(Calendar.YEAR, date.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, date.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, date.get(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, time.get(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));

		this.setConvertedInput(calendar.getTime());
	}
}
