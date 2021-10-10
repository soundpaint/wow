package org.soundpaint.wow.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.AppConfig;

public class I18NUtils {
  private I18N i18n;
  private DateFormat monthAndDayFormatter;
  private DateFormat yearAndMonthFormatter;
  private DateFormat yearAndNumericMonthFormatter;
  private DateFormat fullDateFormatter;
  private DateFormat fullDateTimeFormatter;
  private NumberFormat longFormatter;
  private NumberFormat doubleFormatter;

  private I18NUtils() {
  }

  public I18NUtils(final I18N i18n) {
    this();
    setI18N(i18n);
  }

  public void setI18N(final I18N i18n) {
    if (i18n == null)
      throw new NullPointerException("i18n");
    this.i18n = i18n;
  }

  public void i18nChangedLocale() {
    monthAndDayFormatter =
        new SimpleDateFormat(i18n.get_dateFormat_monthAndDay(),
            i18n.getLocale());
    yearAndMonthFormatter =
        new SimpleDateFormat(i18n.get_dateFormat_yearAndMonth(),
            i18n.getLocale());
    yearAndNumericMonthFormatter =
        new SimpleDateFormat(i18n.get_dateFormat_yearAndNumericMonth(),
            i18n.getLocale());
    fullDateFormatter =
        new SimpleDateFormat(i18n.get_dateFormat_fullDate(), i18n.getLocale());
    fullDateTimeFormatter =
        new SimpleDateFormat(i18n.get_dateFormat_fullDateTime(),
            i18n.getLocale());
    longFormatter = NumberFormat.getIntegerInstance(i18n.getLocale());
    doubleFormatter = NumberFormat.getNumberInstance(i18n.getLocale());
  }

  public String formatMonthAndDay(final Date date) {
    return monthAndDayFormatter.format(date);
  }

  public Date parseMonthAndDay(final String strValue) throws ParseException {
    return monthAndDayFormatter.parse(strValue);
  }

  public String formatYearAndNumericMonth(final Date date) {
    return yearAndNumericMonthFormatter.format(date);
  }

  public Date parseYearAndNumericMonth(final String strValue)
      throws ParseException {
    return yearAndNumericMonthFormatter.parse(strValue);
  }

  public String formatYearAndMonth(final Date date) {
    return yearAndMonthFormatter.format(date);
  }

  public Date parseYearAndMonth(final String strValue) throws ParseException {
    return yearAndMonthFormatter.parse(strValue);
  }

  public String formatFullDate(final Date date) {
    return fullDateFormatter.format(date);
  }

  public Date parseFullDate(final String strValue) throws ParseException {
    return fullDateFormatter.parse(strValue);
  }

  public String formatFullDateTime(final Date date) {
    return fullDateTimeFormatter.format(date);
  }

  public Date parseFullDateTime(final String strValue) throws ParseException {
    return fullDateTimeFormatter.parse(strValue);
  }

  public String formatDateRange(final String from, final String to) {
    return i18n.get_dateFormat_dateRange(from, to);
  }

  public long parseLongValue(final String strValue) throws ParseException {
    return longFormatter.parse(strValue).longValue();
  }

  public String formatLongValue(final long value) {
    return longFormatter.format(value);
  }

  public double parseDoubleValue(final String strValue) throws ParseException {
    return doubleFormatter.parse(strValue).doubleValue();
  }

  public String formatDoubleValue(final double value) {
    return doubleFormatter.format(value);
  }

  private static String currencySymbol;

  static {
    AppConfig userProps = AppConfig.getDefault();
    currencySymbol = userProps.get_i18n_currencySymbol();
  }

  public String formatCurrencyValue(final double value,
      final String preferredCurrencySymbol) {
    final double normalizedValue;
    if (value == -0.0) {
      normalizedValue = 0.0;
    } else {
      normalizedValue = value;
    }
    final String currencySymbol;
    if (preferredCurrencySymbol != null) {
      currencySymbol = preferredCurrencySymbol;
    } else {
      currencySymbol = I18NUtils.currencySymbol;
    }
    return String.format(i18n.getLocale(), i18n.get_currencyFormat(),
        normalizedValue, currencySymbol);
  }

  // TODO: public double parseCurrencyValue(final String strValue);

  public String formatBoolean(final boolean value) {
    return value ? i18n.get_booleanValueTrue() : i18n.get_booleanValueFalse();
  }

  public boolean parseBoolean(String strValue) throws ParseException {
    final boolean value;
    if (strValue.equalsIgnoreCase(i18n.get_booleanValueTrue())) {
      value = true;
    } else if (strValue.equalsIgnoreCase(i18n.get_booleanValueFalse())) {
      value = false;
    } else {
      throw new ParseException("failed parsing localized value \"" + strValue
          + "\": not a Boolean value", 0);
    }
    return value;
  }

  public String formatYesNo(final boolean value) {
    return value ? i18n.get_booleanValueYes() : i18n.get_booleanValueNo();
  }

  public boolean parseYesNo(final String strValue) throws ParseException {
    final boolean value;
    if (strValue.equalsIgnoreCase(i18n.get_booleanValueYes())) {
      value = true;
    } else if (strValue.equalsIgnoreCase(i18n.get_booleanValueNo())) {
      value = false;
    } else {
      throw new ParseException("failed parsing localized value \"" + strValue
          + "\": not a Boolean yes/no value", 0);
    }
    return value;
  }

  // TODO: Add a generic formatter and parser for arbitrary
  // enumeration types, backed by the enumeration database table.
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
