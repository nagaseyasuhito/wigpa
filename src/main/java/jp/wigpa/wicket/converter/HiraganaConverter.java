package jp.wigpa.wicket.converter;

import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.wicket.util.convert.converter.AbstractConverter;

import com.ibm.icu.text.Transliterator;

public class HiraganaConverter extends AbstractConverter<CharSequence> {
	private static final Transliterator HALFWIDTH_TO_FULLWIDTH = Transliterator.getInstance("Halfwidth-Fullwidth");
	private static final Pattern HIRAGANA_PATTERN = Pattern.compile("^[ぁ-んゔ ]*$");
	private static final Transliterator KATAKANA_TO_HIRAGANA = Transliterator.getInstance("Katakana-Hiragana");

	private static final long serialVersionUID = 1L;

	@Override
	public CharSequence convertToObject(String value, Locale locale) {
		String result = HiraganaConverter.HALFWIDTH_TO_FULLWIDTH.transform(value);
		result = HiraganaConverter.KATAKANA_TO_HIRAGANA.transform(result);
		result = result.replaceAll("　", " ");

		if (!HiraganaConverter.HIRAGANA_PATTERN.matcher(result).matches()) {
			throw this.newConversionException("Cannot convert to hiragana", value, locale).setResourceKey("HiraganaConverter");
		}
		return result;
	}

	@Override
	protected Class<CharSequence> getTargetType() {
		return CharSequence.class;
	}
}
