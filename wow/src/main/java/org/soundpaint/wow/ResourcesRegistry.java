package org.soundpaint.wow;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcesRegistry
{
  private static final Logger logger =
    LogManager.getLogger(ResourcesRegistry.class);

  private static final ResourcesRegistry defaultInstance =
    new ResourcesRegistry();

  private final HashMap<String, Class<? extends Resource>> resources;

  public static ResourcesRegistry getDefaultInstance()
  {
    return defaultInstance;
  }

  private ResourcesRegistry()
  {
    resources = new HashMap<String, Class<? extends Resource>>();
  }

  /**
   * Creates a default page path and class name from the specified template
   * name.
   * 
   * @param templateName
   */
  public void register(final String templateName, final String pagesPackage)
  {
    final String path = "/" + templateName;
    // Get Resource class object.
    final Class<?> resourceClass;
    final String classPrefix =
        !pagesPackage.isEmpty() ? pagesPackage + "." : "";
    final String fullClassName = classPrefix + templateName.replace('/', '.');
    try {
      resourceClass = Class.forName(fullClassName);
    } catch (final ExceptionInInitializerError e) {
      // fatal error in user-provided code
      logger.error("failed getting class " + fullClassName + ": " + e, e);
      throw new RuntimeException(e);
    } catch (final LinkageError e) {
      // should never occur (or error in system administration)
      logger.error("failed getting class " + fullClassName + ": " + e, e);
      throw new RuntimeException(e);
    } catch (final ClassNotFoundException e) {
      logger.error("failed getting class " + fullClassName + ": " + e, e);
      throw new RuntimeException(e);
    }
    if (resourceClass == null) {
      final String errorMessage =
        "resources registry: " +
        "no handler found for registering resource \"" + path + "\"";
      logger.error(errorMessage);
      throw new InternalError(errorMessage);
    }
    if (!Resource.class.isAssignableFrom(resourceClass)) {
      final String errorMessage =
        "resource handler " + resourceClass +
        " is not a subclass of the Resource class";
      logger.error(errorMessage);
      throw new InternalError(errorMessage);
    }
    register(path, (Class<? extends Resource>)resourceClass);
  }

  public void register(final String path,
                       final Class<? extends Resource> resource)
  {
    logger.info("registering handler " + resource + " for resource " + path);
    resources.put(path, resource);
  }

  public Class<? extends Resource> deregister(final String path)
  {
    logger.info("deregistering handler for resource " + path);
    return resources.remove(path);
  }

  public Class<? extends Resource> lookup(final String path)
  {
    if (!resources.containsKey(path)) {
      final String message =
        "resources registry: " +
        "no handler available for resource \"" + path + "\"; " +
        "supported resources: " + listKeys();
      logger.error(message);
      throw new InternalError(message);
    }
    return resources.get(path);
  }

  /**
   * For debugging purposes.
   */
  public String listKeys()
  {
    final StringBuffer keys = new StringBuffer();
    for (final String key : resources.keySet()) {
      if (keys.length() > 0) {
        keys.append(", ");
      }
      keys.append("\"" + key + "\"");
    }
    return "keys={" + keys + "}";
  }

  public String toString()
  {
    return "ResourcesRegistry[" + listKeys() + "]";
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
