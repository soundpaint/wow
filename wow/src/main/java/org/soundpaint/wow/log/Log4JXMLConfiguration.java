package org.soundpaint.wow.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class Log4JXMLConfiguration extends XmlConfiguration
{
  /**
   * N.B. This file path of the default log is intentionally not be made
   * configurable, since (1) changing the path while the system is running may
   * result in strange behavior, and (2) more serious, looking up this file path
   * in the runtime configuration can be done only after the runtime
   * configuration has been loaded, such that the right order of class
   * initialization becomes important, but currently can not be safely
   * guaranteed when unexpected exception are thrown that change the code path
   * during initialization; specifically, crashing during class initialization
   * while serving the very first page will cause an error page to be served
   * which itself will cause the initialization to be triggered another time,
   * which will very likely lead to the same crash again, etc.
   */
  private static final String FALLBACK_LOGFILE_PATH = "/tmp/web.log";

  public Log4JXMLConfiguration(final LoggerContext context,
                               final ConfigurationSource configSource)
  {
    super(context, configSource);
  }
 
  @Override
  protected void doConfigure()
  {
    super.doConfigure();
    final LoggerContext context = (LoggerContext)LogManager.getContext(false);
    final Configuration config = context.getConfiguration();
    final PatternLayout.Builder layoutBuilder =
      PatternLayout.newBuilder().
      withConfiguration(config).
      withPattern("%d %-4r [%t] %-5p %c %x - %m%n").
      withDisableAnsi(false).
      withNoConsoleNoAnsi(true).
      withAlwaysWriteExceptions(true).
      withHeader(null).
      withFooter(null);
    final PatternLayout layout = layoutBuilder.build();
    final FileAppender.Builder<?> appenderBuilder =
      FileAppender.newBuilder().
      withFileName(FALLBACK_LOGFILE_PATH).
      withAppend(true).
      withImmediateFlush(true).
      setConfiguration(config).
      setLayout(layout).
      setIgnoreExceptions(false).
      setName("File");
    final FileAppender appender = appenderBuilder.build();
    config.addAppender(appender);
    Configurator.initialize(config);
    Configurator.setRootLevel(Level.INFO);
    final Logger logger = LogManager.getLogger(Log4JXMLConfiguration.class);
    logger.info("log4j initialized");
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
