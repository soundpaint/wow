package org.soundpaint.wow.i18n;

import org.soundpaint.wow.utils.Ident;
import org.soundpaint.wow.utils.ParseException;

public class ArgIdent extends Ident {
  private ArgIdent(final String value) {
    super(value);
  }

  public void accept(final String value) {
    if (value == null)
      throw new NullPointerException("value");
    if (value.length() == 0)
      throw new ParseException("empty identifier");
    for (int i = 0; i < value.length(); i++) {
      final char ch = value.charAt(i);
      if (!Character.isJavaIdentifierPart(ch))
        throw new ParseException("unexpected identifier char '" + ch + "' in '"
            + value + "'");
    }
  }

  public static ArgIdent parse(final String value) {
    return new ArgIdent(value);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
