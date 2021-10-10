package org.soundpaint.wow;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {
  private LogUtils() {
  }

  public static String getStackTrace(final Throwable t) {
    final StringWriter s = new StringWriter();
    final PrintWriter p = new PrintWriter(s);
    t.printStackTrace(p);
    p.flush();
    final String stackTrace = s.toString();
    p.close();
    try {
      s.close();
    } catch (final Exception e) {
    }
    return stackTrace;
  }

  public static String createStackTrace() {
    return getStackTrace(new Exception());
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
