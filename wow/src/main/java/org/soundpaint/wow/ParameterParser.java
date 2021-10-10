package org.soundpaint.wow;

import java.io.IOException;

import org.soundpaint.wow.utils.ParseException;

import jakarta.servlet.ServletException;

public interface ParameterParser
{
  void parse(final Resource resource,
             final String keyPrefix,
             final String keySuffix,
             final String[] values)
    throws ParseException, IOException, ServletException;

  void finishParse(final Resource resource);
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
