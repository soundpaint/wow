package org.soundpaint.wow.utils;

public class SimpleIdent extends Ident {
  protected SimpleIdent(final String value) {
    super(value);
  }

  public static int maxAcceptableChars(final String value) {
    if (value == null) {
      return -1;
    } else if (value.length() == 0) {
      return 0;
    } else if (!Character.isJavaIdentifierStart(value.charAt(0))) {
      return 0;
    } else {
      for (int pos = 1; pos < value.length(); pos++) {
        if (!Character.isJavaIdentifierPart(value.charAt(pos))) { return pos; }
      }
      return value.length();
    }
  }

  protected void accept(final String value) {
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
  }

  public static SimpleIdent parse(final String value) {
    return new SimpleIdent(value);
  }

  public boolean equals(final Object obj) {
    final boolean result;
    if (!(obj instanceof SimpleIdent)) {
      result = false;
    } else {
      result = super.equals(obj);
    }
    return result;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
