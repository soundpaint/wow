package org.soundpaint.wow.auth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.db.types.DBGGUID;
import org.soundpaint.wow.utils.ParseException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class User
{
  private static final Logger logger = LogManager.getLogger(User.class);
  private static final boolean REQUIRE_SALT_VALUE = Boolean.FALSE;

  public static final String USERNAME_OF_ROOT = "admin";

  private final String loginName;
  private final Credential credential;
  private final boolean enabled;
  private final URI defaultPageURI;
  private final DBGGUID userGGUID;
  private final HashSet<Role> roles;

  private User(final String loginName,
               final Credential credential,
               final boolean enabled,
               final URI defaultPageURI,
               final DBGGUID userGGUID)
  {
    if (loginName == null)
      throw new NullPointerException("loginName");
    if (credential == null)
      throw new NullPointerException("credential");
    this.loginName = loginName;
    this.credential = credential;
    this.enabled = enabled;
    this.defaultPageURI = defaultPageURI;
    this.userGGUID = userGGUID;
    roles = new HashSet<Role>();
  }

  public static User create(final String loginName,
                            final Credential.HashMethod hashMethod,
                            final String passwordToken,
                            final String saltValue,
                            final boolean enabled,
                            final URI defaultPageURI,
                            final DBGGUID userGGUID)
  {
    final Credential credential =
      new Credential(hashMethod, passwordToken, saltValue);
    final User user =
      new User(loginName, credential, enabled, defaultPageURI, userGGUID);
    logger.info("user " + loginName + " uses " +
                user.getCredential().getHashMethod() + " password coding");
    return user;
  }

  public static User fromXMLElement(final Element userNode,
                                    final RolesConfig rolesConfig)
    throws IOException
  {
    if (!userNode.getTagName().equals("user"))
      throw new ParseException("user node expected");
    final String idAttr = userNode.getAttribute("id");
    if (idAttr.length() == 0)
      throw new ParseException("empty 'id' attribute");
    final String loginName = idAttr;
    final String startPageAttr = userNode.getAttribute("start-page");
    final URI defaultPageURI;
    try {
      defaultPageURI =
        startPageAttr.length() != 0 ? new URI(startPageAttr) : null;
    } catch (final URISyntaxException ex) {
      throw new IOException(ex);
    }
    final Element passwordNode =
      (Element) userNode.getElementsByTagName("password").item(0);
    final Credential.HashMethod hashMethod =
      Credential.HashMethod.fromId(passwordNode.getAttribute("coding"));
    logger.debug("user " + loginName + " determined to use " + hashMethod +
                 " password coding"); // DEBUG
    final String saltValue = userNode.getAttribute("salt");
    if (REQUIRE_SALT_VALUE && (saltValue.length() == 0))
      throw new ParseException("empty 'salt' attribute");
    final String passwordHash = passwordNode.getTextContent();
    final User user =
      create(loginName, hashMethod, passwordHash, saltValue, true,
             defaultPageURI, null);
    final NodeList roleNodes = userNode.getElementsByTagName("role");
    for (int j = 0; j < roleNodes.getLength(); j++) {
      final Element roleNode = (Element) roleNodes.item(j);
      final RoleIdent roleId = new RoleIdent(roleNode.getAttribute("ref"));
      final Role role = rolesConfig.lookupRole(roleId);
      user.addRole(role, false, null);
    }
    return user;
  }

  public String getLoginName()
  {
    return loginName;
  }

  public Credential getCredential()
  {
    return credential;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public URI getDefaultPageURI()
  {
    return defaultPageURI;
  }

  public DBGGUID getUserGGUID()
  {
    return userGGUID;
  }

  /**
   * Only user data records in the database can be changed through this API.
   * User data that is defined in the XML configuration files, can only be
   * changed by editing these files.
   */
  public boolean canModify()
  {
    return userGGUID != null;
  }

  public boolean canAuthenticate(final String password)
  {
    logger.info("checking password for user " + loginName + " and "+
                credential.getHashMethod() + " password coding");
    return credential.canAuthenticate(password);
  }

  public boolean changePassword(final String oldPassword,
                                final String newPassword,
                                final String newSaltValue)
  {
    if (!canModify())
      throw new InternalError("can not change password of user defined in " +
                              "XML configuration files");
    final boolean changed =
      credential.changePassword(oldPassword, newPassword, newSaltValue);
    if (!changed)
      return false;
    // TODO: Update password also in database.
    throw new InternalError("password change in db not yet implemented");
  }

  public void addRole(final Role role, final Connection connection)
    throws IOException
  {
    addRole(role, true, connection);
  }

  public void addRole(final Role role,
                      final boolean updateDB,
                      final Connection connection)
    throws IOException
  {
    addRoles(new Role[] { role }, updateDB, connection);
  }

  public void addRoles(final Role[] roles,
                       final boolean updateDB,
                       final Connection connection)
    throws IOException
  {
    if (roles.length == 0) {
      logger.warn("adding empty set of roles to user " + getLoginName());
      return;
    }
    if (updateDB && !canModify())
      throw new InternalError("can not change roles of user defined in " +
                              "XML configuration files");
    for (final Role role : roles) {
      if (!this.roles.contains(role)) {
        this.roles.add(role);
      } else {
        System.out.println("Warning: duplicate role inclusion (" +
                           role.getId().toSource() + ") for user " +
                           getLoginName());
      }
      // TODO: It should not be possible to assign a role that is defined in the
      // XML configuration file to a user that is defined in database.
      // Otherwise, silently removing a role from the XML configuration file may
      // corrupt lots of user accounts in the database.
    }

    if (updateDB) {
      final List<DBGGUID> roleGGUIDs = new ArrayList<DBGGUID>();
      for (int i = 0; i < roles.length; i++) {
        final DBGGUID roleGGUID = roles[i].getGGUID();
        if (roleGGUID == null) {
          throw new InternalError("roles data corrupted: " +
                                  "got null GGUID for role " + roles[i] +
                                  "; user: " + this + ")");
        }
        roleGGUIDs.add(roleGGUID);
      }
      // User records are not permanently hold in memory, as their number may
      // become large. Hence, fetch the user from the database.
      // final DBGGUID userGGUID = getUserGGUID(); // TODO
      // TODO: Merge contents of roleGGUIDs into those stored in
      // user record for persistent storage in DB
    }
  }

  public void removeRole(final Role role, final Connection connection)
    throws IOException
  {
    removeRole(role, true, connection);
  }

  public void removeRole(final Role role,
                         final boolean updateDB,
                         final Connection connection)
    throws IOException
  {
    removeRoles(new Role[] { role }, updateDB, connection);
  }

  public void removeRoles(final Role[] roles,
                          final boolean updateDB,
                          final Connection connection)
    throws IOException
  {
    if (roles.length == 0) {
      logger.warn("removing empty set of roles from user " + getLoginName());
      return;
    }
    if (updateDB && !canModify())
      throw new InternalError("can not change roles of user defined in " +
                              "XML configuration files");
    for (final Role role : roles) {
      if (this.roles.contains(role)) {
        this.roles.remove(role);
      } else {
        System.out.println("Warning: no role (" + role.getId().toSource() +
                           ") for removal from user " + getLoginName());
      }
      // TODO: It should not be possible to assign a role that is defined in the
      // XML configuration file to a user that is defined in database.
      // Otherwise, silently removing a role from the XML configuration file may
      // corrupt lots of user accounts in the database.
    }

    if (updateDB) {
      final List<DBGGUID> roleGGUIDs = new ArrayList<DBGGUID>();
      for (int i = 0; i < roles.length; i++) {
        final DBGGUID roleGGUID = roles[i].getGGUID();
        if (roleGGUID == null) {
          throw new InternalError("roles data corrupted: " +
                                  "got null GGUID for role " +
                                  roles[i].toString());
        }
        roleGGUIDs.add(roleGGUID);
      }
      // User records are not permanently hold in memory, as their number may
      // become large. Hence, fetch the user from the database.
      // final DBGGUID userGGUID = getUserGGUID(); // TODO
      // TODO: Remove contents of roleGGUIDs from those stored in
      // user record for persistent storage in DB
    }
  }

  public Collection<Role> getRoles()
  {
    return roles;
  }

  private String rolesToString()
  {
    final StringBuffer s = new StringBuffer();
    for (final Role role : roles) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(role.toString());
    }
    return "[" + s + "]";
  }

  public String toString()
  {
    return
      "User[loginName=" + loginName +
      ", credential=" + credential +
      ", enabled=" + enabled +
      ", defaultPageURI=" + defaultPageURI +
      ", gguid=" + userGGUID.asSQLString() +
      ", roles=" + rolesToString() + "]";
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
