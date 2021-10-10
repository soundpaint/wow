package org.soundpaint.wow.db.types;

import org.soundpaint.wow.utils.ParseException;

public class DBIdent {
  private String value;

  private DBIdent() {
  }

  private DBIdent(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static DBIdent parse(String value) {
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
    return new DBIdent(value);
  }

  public String toSQL() {
    return "`" + value + "`";
  }

  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof DBIdent))
      return false;
    DBIdent other = (DBIdent) obj;
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
