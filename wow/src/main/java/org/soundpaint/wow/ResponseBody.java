package org.soundpaint.wow;

import java.io.IOException;

public abstract class ResponseBody {
  public abstract void write() throws IOException;

  public abstract void flush() throws IOException;
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
