package org.soundpaint.wow;

import java.io.IOException;

import org.soundpaint.wow.utils.ParseException;

public abstract class SingleParameterParser implements ParameterParser
{
  protected SingleParameterParser() {}

  public void parse(final Resource resource,
                    final String keyPrefix,
                    final String keySuffix,
                    final String[] values)
    throws ParseException, IOException
  {
    if (keySuffix.length() > 0)
      throw new IOException("single value parameters can not be indexed: " +
                            keyPrefix + "." + keySuffix);
    if (values.length > 1)
      throw new IOException(
          "single value parameter can not occur multiple times: " + keyPrefix);
    assert values.length > 0 : "parameter key without parameter value";
    parseValue(resource, values[0]);
  }

  protected abstract void parseValue(final Resource resource, final String value)
    throws ParseException, IOException;

  public void finishParse(final Resource resource) {}
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
