package org.soundpaint.wow.auth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.util.security.MD5Encoder;
import org.soundpaint.wow.AppConfig;
import org.soundpaint.wow.db.types.DBGGUID;
import org.soundpaint.wow.utils.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Holds the set of user accounts.
 */
public class UsersConfig
{

  private static final URI DEFAULT_START_PAGE;

  static
  {
    try {
      DEFAULT_START_PAGE =
        new URI(AppConfig.getDefault().get_uri_defaultStartPage());
    } catch (final URISyntaxException e) {
      throw new RuntimeException("failed initializing default start page URI");
    }
  }

  private final RolesConfig rolesConfig;
  private final HashMap<String, User> loginName2User;
  private final HashMap<DBGGUID, User> userGGUID2User;

  private UsersConfig(final Document document, final RolesConfig rolesConfig)
    throws IOException
  {
    if (rolesConfig == null)
      throw new NullPointerException("rolesConfig");
    this.rolesConfig = rolesConfig;
    loginName2User = new HashMap<String, User>();
    userGGUID2User = new HashMap<DBGGUID, User>();
    final Element usersNode = document.getDocumentElement();
    if (!usersNode.getTagName().equals("users"))
      throw new ParseException("bad document root element: \"" +
                               usersNode.getTagName() +
                               "\" (expected: \"users\")");
    final NodeList userNodes = usersNode.getElementsByTagName("user");
    for (int i = 0; i < userNodes.getLength(); i++) {
      final Element userNode = (Element) userNodes.item(i);
      final User user = User.fromXMLElement(userNode, rolesConfig);
      final String loginName = user.getLoginName();
      if (loginName2User.containsKey(loginName))
        throw new ParseException("duplicate user: " + loginName);
      loginName2User.put(loginName, user);
    }
    // In contrast to role data, user data is *not* pre-loaded from the
    // database into main memory, since the number of users may become
    // very large.
  }

  private User loadUserDataFromDB(final Connection connection,
                                  final String loginName)
    throws IOException
  {
    // TODO: get userGGUID from user record that matches given login name
    return loadUserDataFromDB(connection, DBGGUID.create());
  }

  private User loadUserDataFromDB(final Connection connection,
                                  final DBGGUID userGGUID)
    throws IOException
  {
    // TODO
    final String loginName = "admin";
    final String passwordHash = MD5Encoder.encode("admin".getBytes());
    final String saltValue = "2021-09-28_23:55:00";
    final boolean enabled = true;
    final URI defaultPageURI = DEFAULT_START_PAGE;
    final User user =
      User.create(loginName, Credential.HashMethod.MD5, passwordHash,
                  saltValue, enabled, defaultPageURI, userGGUID);
    final DBGGUID roleGGUID = null; // TODO: fetch GGUIDs of user roles from user record
    final Role role = rolesConfig.lookupRole(roleGGUID);
    user.addRole(role, false, null);
    return user;
  }

  public static UsersConfig parseFromURI(final URI configFile,
                                         final RolesConfig rolesConfig)
    throws IOException
  {
    final Document document;
    try {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating(false);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      document = db.parse(configFile.toString());
      return new UsersConfig(document, rolesConfig);
    } catch (final Exception e) {
      throw new IOException("parsing users config xml " + configFile +
                            " failed: " + e.getMessage() + "\r\n" +
                            configFile, e);
    }
  }

  public User lookupUser(final Connection connection, final String loginName)
    throws IOException
  {
    final User user;
    final User userByLoginName = loginName2User.get(loginName);
    if (userByLoginName != null) {
      user = userByLoginName;
    } else {
      // if not in pre-loaded set, try fetching from database
      user = loadUserDataFromDB(connection, loginName);
    }
    return user;
  }

  public User lookupUser(final Connection connection,
                         final DBGGUID userGGUID)
    throws IOException
  {
    final User user;
    final User userByGGUID = userGGUID2User.get(userGGUID);
    if (userByGGUID != null) {
      user = userByGGUID;
    } else {
      // if not in pre-loaded set, try fetching from database
      user = loadUserDataFromDB(connection, userGGUID);
    }
    return user;
  }

  public Collection<User> getUsers()
  {
    return loginName2User.values();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
