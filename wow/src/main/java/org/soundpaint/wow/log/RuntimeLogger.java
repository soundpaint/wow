package org.soundpaint.wow.log;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

public class RuntimeLogger
{
  private static final String LOGGING_PROPERTIES_PATH =
      "../../../../../logging.properties";

  private static final Logger logger;

  static
  {
    System.out.println("INFO: Setting up log4j configuration from " +
                       RuntimeLogger.class.getResource(LOGGING_PROPERTIES_PATH));
    final InputStream inputStream =
      RuntimeLogger.class.getResourceAsStream(LOGGING_PROPERTIES_PATH);
    try {
      if (inputStream == null) {
        throw new IOException("logging.properties not found");
      }
      final LoggerContext context = LoggerContext.getContext();
      final ConfigurationSource source = new ConfigurationSource(inputStream);
      final Configuration config =
        new Log4JXMLConfigurationFactory().getConfiguration(context, source);
      Configurator.initialize(config);
      System.out.println("INFO: Successfully loaded log4j configuration");
    } catch (final IOException e) {
      // logging.properties not found or not accessible => use default cfg only
      System.out.println("ERROR: Failed loading log4j config, " +
                         "Fallback to system default: " + e.getMessage());
      Configurator.initialize(new DefaultConfiguration());
    }
    logger = LogManager.getLogger(RuntimeLogger.class);
    logger.info("RuntimeLogger initialized");
  }

  /**
   * Call this method at any time to force the static initializer of
   * this class to be called, but only once (i.e. only if the static
   * initializer has not already been executed since loading of this
   * class).
   */
  public static void init() {}
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
