package org.soundpaint.wow.utils;

public class ParseException extends RuntimeException {
  private static final long serialVersionUID = 0L;

  public ParseException(String message) {
    super("parse error: " + message);
  }

  public ParseException(String message, Exception source) {
    super("parse error: " + source.getMessage() + ":\r\n" + message);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
