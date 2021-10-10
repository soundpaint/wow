package org.soundpaint.wow;

import java.text.ParseException;

public abstract class UserProperty
{
  public static enum Type
  {
    STRING, DATE, BOOLEAN;
  };

  private String key;
  private String description;
  private Type type;
  private short typeSize;

  private UserProperty() {}

  protected UserProperty(final String key,
                         final String description,
                         final Type type,
                         final short typeSize)
  {
    this();
    this.key = key;
    this.description = description;
    this.type = type;
    this.typeSize = typeSize;
  }

  public String getKey()
  {
    return key;
  }

  public abstract String formatValue();

  public abstract void parseValue(final String value) throws ParseException;

  public String getDescription()
  {
    return description;
  }

  // TODO: Do we need the following explicit type
  // information? Actually, it is already implicit
  // available through the values' data types.
  public Type getType()
  {
    return type;
  }

  public short getTypeSize()
  {
    return typeSize;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
