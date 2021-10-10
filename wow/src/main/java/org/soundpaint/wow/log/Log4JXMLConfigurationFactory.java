package org.soundpaint.wow.log;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Log4JXMLConfigurationFactory", category = "ConfigurationFactory")
@Order(10)
/**
 * This factory and its companion class Log4JXMLConfiguration loosely
 * follow the example code presented in Sect. "Initialize Log4j by
 * Combining Configuration File with Programmatic Configuration" of
 * the Apache Log4j 2 documentation at
 * https://logging.apache.org/log4j/2.x/manual/customconfig.html.
 * 
 * Note that, presumably, the example code was written against an
 * older API version such that it does not even compile any more.
 * I have modified the example code accordingly to match the
 * newer API of Log4j 2.
 */
public class Log4JXMLConfigurationFactory extends ConfigurationFactory
{
  /**
   * Valid file extensions for XML files.
   */
  public static final String[] SUFFIXES = new String[] { ".xml", "*" };

  /**
   * Return the Configuration.
   * @param source The logger context.
   * @param source The input source.
   * @return The configuration.
   */
  @Override
  public Configuration getConfiguration(final LoggerContext loggerContext,
                                        final ConfigurationSource source)
  {
    return new Log4JXMLConfiguration(loggerContext, source);
  }

  /**
   * Returns the file suffixes for XML files.
   * @return An array of File extensions.
   */
  public String[] getSupportedTypes()
  {
    return SUFFIXES;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
