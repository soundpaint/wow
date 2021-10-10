package org.soundpaint.wow;

public class InternalException extends RuntimeException
{
  private static final long serialVersionUID = -1778968467545023333L;

  public InternalException(final String message, final I18N i18n)
  {
    super(message);
  }

  public InternalException(final String message,
                           final I18N i18n,
                           final Throwable source)
  {
    this(message + ": " + source.getMessage(), i18n);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
