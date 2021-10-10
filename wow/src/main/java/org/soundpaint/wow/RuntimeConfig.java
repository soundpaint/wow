package org.soundpaint.wow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.auth.RoleIdent;
import org.soundpaint.wow.auth.RolesConfig;
import org.soundpaint.wow.auth.UsersConfig;
import org.soundpaint.wow.log.RuntimeLogger;
import org.soundpaint.wow.navigator.Navigator;
import org.soundpaint.wow.utils.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RuntimeConfig extends Config
{
  static
  {
    // ensure logger has been initialized before doing anything else
    RuntimeLogger.init();
  }

  private static final Logger logger = LogManager.getLogger(RuntimeConfig.class);
  private static final String REL_PATH_TO_ROOT = "../../../..";
  private static final URI rootURI = createURIToRoot();
  private static final RuntimeConfig defaultInstance = createDefaultInstance();

  private static URI createURIToRoot()
  {
    try {
      return RuntimeConfig.class.getResource(REL_PATH_TO_ROOT).toURI();
    } catch (final URISyntaxException e) {
      throw new InternalError("failed determining URI to system root");
    }
  }

  private static RuntimeConfig createDefaultInstance()
  {
    logger.info("creating RuntimeConfig default instance");
    try {
      final RuntimeConfig config = RuntimeConfig.parseFromResource(rootURI);
      return config;
    } catch (final Exception e) {
      logger.error(e + ":\n" + LogUtils.getStackTrace(e));
      throw new InternalError("failed initializing runtime config", e);
    }
  }

  public static RuntimeConfig getDefaultInstance()
  {
    return defaultInstance;
  }

  /**
   * Call this method at any time to force the static initializer of
   * this class to be called, but only once (i.e. only if the static
   * initializer has not already been executed since loading of this
   * class).
   */
  public static void init() {}

  private final String jdbcPath;
  // private NavigationConfig navigationConfig; // TODO
  private final RolesConfig rolesConfig;
  private final UsersConfig usersConfig;
  private final SystemConfig optionsConfig;
  private final Navigator navigator;
  private final RoleIdent defaultRoleId;
  private final URI webRoot;
  private final URI cssURI;
  private final URI shortcutIconURI;

  private RuntimeConfig(final Document document, final URI root)
    throws IOException
  {
    super(document, root);

    logger.info("initializing runtime");

    /*-- DB Configuration --*/
    logger.info("initializing database connection");
    final Element dbAccessNode = getPropertyElement("db-access");
    final Element jdbcPathNode =
      (Element) dbAccessNode.getElementsByTagName("jdbc-path").item(0);
    jdbcPath = jdbcPathNode.getTextContent();

    // TODO:
    // DBUtils.init(getJDBCPath());

    // TODO:
    /*-- Navigation Configuration --*/
    /*
     * logger.info("initializing navigation");
     * final Element navigationConfigNode =
     *   (Element)configNode.getElementsByTagName("navigation-config").item(0);
     * final String navigationConfigFilePath =
     *   navigationConfigNode.getAttribute("file-path");
     * navigationConfig =
     *   NavigationConfig.parseFromURI(root.resolve(navigationConfigFilePath));
     */

    /*-- Roles Configuration --*/
    logger.info("initializing roles");
    final Element defaultRoleNode = getPropertyElement("default-role");
    defaultRoleId = new RoleIdent(defaultRoleNode.getAttribute("ref"));
    final Element rolesConfigNode = getPropertyElement("roles-config");
    final String rolesConfigFilePath =
      rolesConfigNode.getAttribute("file-path");
    rolesConfig =
      RolesConfig.loadConfig(root.resolve(rolesConfigFilePath),
                             getDefaultRoleId());

    /*-- Users Configuration --*/
    logger.info("initializing users");
    final Element usersConfigNode = getPropertyElement("users-config");
    final String usersConfigFilePath =
      usersConfigNode.getAttribute("file-path");
    usersConfig =
      UsersConfig.parseFromURI(root.resolve(usersConfigFilePath), rolesConfig);

    /*-- Options Configuration --*/
    logger.info("initializing options");
    optionsConfig = SystemConfig.getDefault(); // TODO: Parse from URI.

    /*-- Web Root --*/
    final Element webRootNode = getPropertyElement("web-root");
    webRoot = URI.create(webRootNode.getAttribute("uri"));

    /*-- CSS URI --*/
    final Element cssURINode = getPropertyElement("css");
    cssURI = webRoot.resolve(URI.create(cssURINode.getAttribute("uri")));

    /*-- Shortcut Icon URI --*/
    final Element shortcutIconURINode = getPropertyElement("shortcut-icon");
    shortcutIconURI =
      webRoot.resolve(URI.create(shortcutIconURINode.getAttribute("uri")));

    /*-- Navigator Configuration --*/
    logger.info("initializing navigator");
    Navigator.initClass(webRoot);
    navigator = Navigator.getDefault();
  }

  private static RuntimeConfig parseFromResource(final URI root)
    throws IOException
  {
    final URI resource = root.resolve("config.xml");
    final Document document;
    try {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating(false);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      document = db.parse(resource.toString());
    } catch (final Exception e) {
      throw new IOException("parsing global config xml failed: " +
                            e.getMessage() + "\r\n" + resource, e);
    }
    return new RuntimeConfig(document, root);
  }

  public String getJDBCPath()
  {
    return jdbcPath;
  }

  /*
   * TODO:
   * public NavigationConfig getNavigationConfig() { return navigationConfig; }
   */

  public RolesConfig getRolesConfig()
  {
    return rolesConfig;
  }

  public UsersConfig getUsersConfig()
  {
    return usersConfig;
  }

  public SystemConfig getOptionsConfig()
  {
    return optionsConfig;
  }

  public Navigator getNavigator()
  {
    return navigator;
  }

  public RoleIdent getDefaultRoleId()
  {
    return defaultRoleId;
  }

  public URI getWebRoot()
  {
    return webRoot;
  }

  public URI getCSSURI()
  {
    return cssURI;
  }

  public URI getShortcutIconURI()
  {
    return shortcutIconURI;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
