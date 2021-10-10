package org.soundpaint.wow;

/**
 * System configuration properties are those properties that are
 * referenced by the core system, even if there are no templates
 * at all defined.
 * 
 * The system configuration consists of a fixed
 * set of configuration properties that may change only when a
 * new version of the core Web system is released.  This set does
 * not change by introducing new templates or writing new
 * template stubs.
 * 
 * TODO: For type strength, it's OK to provide options as explicitly
 * named constants.  However, the option variables' values should be
 * read from a configuration file rather than merely hard-coding them
 * here.
 *
 * TODO: Actually, (1) there should be an XML file that lists all
 * supported options, their data type and their default values;
 * (2) this java source file should be automatically generated
 * from the XML file; (3) the actual option values should be
 * read from the database, such that the system administrator
 * may change them without need to re-compile.
 * 
 * TODO: We really should define each configuration property by
 * defining a Java annotation, such that the set of all system
 * properties can be easily iterated through, e.g. when loading
 * property values from or saving them to a database.
 */
public class SystemConfig {
  private static SystemConfig defaultInstance = new SystemConfig();

  public static SystemConfig getDefault() {
    return defaultInstance;
  }

  public boolean showDetailedInternalErrors = true;
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
