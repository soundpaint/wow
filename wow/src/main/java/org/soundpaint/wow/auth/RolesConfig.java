package org.soundpaint.wow.auth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.db.types.DBGGUID;
import org.soundpaint.wow.utils.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RolesConfig
{
  private static final Logger logger = LogManager.getLogger(RolesConfig.class);

  private final URI configFile;
  private final RoleIdent defaultRoleId;
  private final HashMap<RoleIdent, Role> roleId2Role;
  private final HashMap<DBGGUID, Role> roleGGUID2Role;
  private Role defaultRole;

  public static RolesConfig loadConfig(final URI configFile,
                                       final RoleIdent defaultRoleId)
    throws IOException
  {
    try {
      return new RolesConfig(configFile, defaultRoleId);
    } catch (final Exception e) {
      throw new IOException("reading roles configuration failed: " +
                            e.getMessage(), e);
    }
  }

  private RolesConfig(final URI configFile, final RoleIdent defaultRoleId)
    throws IOException, ParserConfigurationException, SAXException
  {
    this.configFile = configFile;
    this.defaultRoleId = defaultRoleId;
    roleId2Role = new HashMap<RoleIdent, Role>();
    roleGGUID2Role = new HashMap<DBGGUID, Role>();
    reload();
  }

  private void loadRolesFromDB() throws IOException
  {
    // TODO
    /*
    final RoleIdent roleId = new RoleIdent("admin");
    final DBGGUID roleGGUID = DBGGUID.create(); // TODO
    URI defaultPageURI;
    try {
      defaultPageURI = new URI("/AdminDashBoard");
    } catch (final URISyntaxException e) {
      throw new IOException("invalid URI for default page: " + e);
      // TODO Auto-generated catch block
    }
    final Role role = new Role(roleId, roleGGUID, defaultPageURI);
    roleId2Role.put(roleId, role);
    roleGGUID2Role.put(roleGGUID, role);
    final PrivilegeSetIdent privilegeSetId =
        new PrivilegeSetIdent("adm");
    final AbstractRole.AbstractPrivilegeSet privilegeSet =
        role.lookupPrivilegeSet(privilegeSetId);
    if (privilegeSet == null) { throw new ParseException(
        "unknown privilege set identifier: " + privilegeSetId); }
    role.addPrivilegeSet(privilegeSet);
    logger.info("loaded role from db: " + role);
    */
  }

  private void loadRolesFromXML() throws IOException
  {
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    final Document document;
    try {
      final DocumentBuilder db = dbf.newDocumentBuilder();
      document = db.parse(configFile.toString());
    } catch (final SAXException e) {
      throw new IOException("parsing roles config xml from file " +
        configFile + " failed: " + e.getMessage(), e);
    } catch (final ParserConfigurationException e) {
      throw new IOException("parsing roles config xml from file " +
        configFile + " failed: " + e.getMessage(), e);
    }
    final Element rolesNode = document.getDocumentElement();
    if (!rolesNode.getTagName().equals("roles"))
      throw new ParseException("error in Roles configuration: " +
                               "bad document root element: \"" +
                               rolesNode.getTagName() +
                               "\" (expected: \"roles\")");
    final NodeList roleNodes = rolesNode.getElementsByTagName("role");
    for (int i = 0; i < roleNodes.getLength(); i++) {
      final Element roleNode = (Element) roleNodes.item(i);
      final RoleIdent roleId = new RoleIdent(roleNode.getAttribute("id"));
      final Element defaultPageNode =
        (Element) roleNode.getElementsByTagName("default-page").item(0);
      final URI defaultPageURI;
      try {
        defaultPageURI = new URI(defaultPageNode.getAttribute("src"));
      } catch (final URISyntaxException e) {
        throw new ParseException("error in Roles configuration: " +
                                 "URI syntax exception: " +
                                 defaultPageNode.getAttribute("src"), e);
      }
      final Role role = new Role(roleId, null, defaultPageURI);
      roleId2Role.put(roleId, role);
      final NodeList privilegeSetNodes =
        roleNode.getElementsByTagName("privilege-set");
      for (int j = 0; j < privilegeSetNodes.getLength(); j++) {
        final Element privilegeSetNode = (Element) privilegeSetNodes.item(j);
        final PrivilegeSetIdent privilegeSetId =
          new PrivilegeSetIdent(privilegeSetNode.getAttribute("ref"));
        final AbstractRole.AbstractPrivilegeSet privilegeSet =
          role.lookupPrivilegeSet(privilegeSetId);
        if (privilegeSet == null) {
          throw new ParseException("unknown privilege set identifier: " +
                                   privilegeSetId);
        }
        role.addPrivilegeSet(privilegeSet);
      }
      logger.info("loaded role from xml: " + role);
    }
  }

  public synchronized void reload() throws IOException
  {
    roleId2Role.clear();
    roleGGUID2Role.clear();
    loadRolesFromXML();
    loadRolesFromDB();
    defaultRole = lookupRole(defaultRoleId);
    if (defaultRole == null)
      throw new ParseException("error in Roles configuration: " +
        "no such default role: " + defaultRoleId.toSource());
  }

  public synchronized Role lookupRole(final RoleIdent id)
  {
    return roleId2Role.get(id);
  }

  public synchronized Role lookupRole(final DBGGUID roleGGUID)
  {
    return roleGGUID2Role.get(roleGGUID);
  }

  public synchronized Collection<Role> getRoles()
  {
    return roleId2Role.values();
  }

  public synchronized Role getDefaultRole()
  {
    return defaultRole;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
