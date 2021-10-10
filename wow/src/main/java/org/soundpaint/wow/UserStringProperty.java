package org.soundpaint.wow;

public class UserStringProperty extends UserProperty
{
  private String value;

  public UserStringProperty(final String key,
                            final String value,
                            final String description,
                            final short typeSize)
  {
    super(key, description, Type.STRING, typeSize);
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(final String value)
  {
    this.value = value;
  }

  public String formatValue()
  {
    return value;
  }

  public void parseValue(final String value)
  {
    this.value = value;
  }

  public void setValue(final Object value)
  {
    if (value instanceof String) {
      setValue((String)value);
    } else {
      throw new InternalError("string property is not a String: " +
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
