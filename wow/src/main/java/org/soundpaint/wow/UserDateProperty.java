package org.soundpaint.wow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.soundpaint.wow.utils.ParseException;

public class UserDateProperty extends UserProperty
{
  private static final DateFormat SQL_DATE_FORMAT =
    new SimpleDateFormat("yyyy-MM-dd");
  
  private Date value;

  public UserDateProperty(final String key,
                          final Date value,
                          final String description)
  {
    super(key, description, Type.DATE, (short)80);
    this.value = value;
  }

  public Date getValue()
  {
    return value;
  }

  public void setValue(final Date value)
  {
    this.value = value;
  }

  public void setValue(final Object value)
  {
    if (value instanceof Date) {
      setValue((Date) value);
    } else {
      throw new InternalError("date property not a date: " + value.getClass());
    }
  }

  public String formatValue()
  {
    return SQL_DATE_FORMAT.format(value);
  }

  public void parseValue(final String value) throws ParseException
  {
    try {
      this.value = SQL_DATE_FORMAT.parse(value);
    } catch (final java.text.ParseException e) {
      throw new ParseException("invalid date specification: " + value, e);
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
