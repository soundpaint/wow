package org.soundpaint.wow;

public class InvalidURLException extends RuntimeException {
  private static final long serialVersionUID = 0L;

  public InvalidURLException(String message, I18N i18n) {
    super(i18n.get_message_error_invalidURL(message));
  }

  public InvalidURLException(String message, I18N i18n, Exception source) {
    super(i18n.get_message_error_invalidURL(message) + ":\r\n"
        + source.getMessage(), source);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
