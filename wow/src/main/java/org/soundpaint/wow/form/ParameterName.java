package org.soundpaint.wow.form;

import org.soundpaint.wow.utils.ParseException;

public class ParameterName implements Comparable<ParameterName> {
  private String value;

  private ParameterName() {
  }

  private ParameterName(String value) {
    this.value = value;
  }

  public static ParameterName parse(String value) {
    // TODO: Syntax check needs to be compliant with the according RFC.
    if (value == null)
      throw new NullPointerException("value");
    if (value.length() == 0)
      throw new ParseException("empty identifier");
    char ch = value.charAt(0);
    if (!Character.isJavaIdentifierStart(ch))
      throw new ParseException("unexpected identifier char '" + ch + "' in '"
          + value + "'");
    for (int i = 1; i < value.length(); i++) {
      ch = value.charAt(i);
      if (!Character.isJavaIdentifierPart(ch))
        throw new ParseException("unexpected identifier char '" + ch + "' in '"
            + value + "'");
    }
    return new ParameterName(value);
  }

  public int compareTo(ParameterName other) {
    return value.compareTo(other.value);
  }

  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof ParameterName))
      return false;
    ParameterName other = (ParameterName) obj;
    return value.equals(other.value);
  }

  public String toString() {
    return value;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
