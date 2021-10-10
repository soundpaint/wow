package org.soundpaint.wow;

import org.soundpaint.wow.utils.ParseException;

public class UserBooleanProperty extends UserProperty
{
  private boolean value;

  public UserBooleanProperty(final String key,
                             final boolean value,
                             final String description)
  {
    super(key, description, Type.BOOLEAN, (short)1);
    this.value = value;
  }

  public boolean getValue()
  {
    return value;
  }

  public void setValue(final boolean value)
  {
    this.value = value;
  }

  public String formatValue()
  {
    return Boolean.toString(value);
  }

  public void parseValue(final String value)
  {
    if (Boolean.toString(true).equals(value)) {
      this.value = true;
    } else if (Boolean.toString(false).equals(value)) {
      this.value = false;
    } else {
      throw new ParseException("invalid Boolean specification: " + value);
    }
  }

  public void setValue(final Object value)
  {
    if (value instanceof Boolean) {
      setValue((Boolean)value);
    } else {
      throw new InternalError("Boolean property not a Boolean: " +
                              value.getClass());
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
