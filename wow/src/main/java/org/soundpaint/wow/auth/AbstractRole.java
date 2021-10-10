package org.soundpaint.wow.auth;

import java.net.URI;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.db.types.DBGGUID;
import org.soundpaint.wow.utils.Ident;

public abstract class AbstractRole {

  private static final Logger logger = LogManager.getLogger(AbstractRole.class);

  protected interface AbstractPrivilegeSet {
    public Ident getId();

    public boolean isGrantedForRole(final AbstractRole role);

    public void updateRoleForThisPrivilege(final AbstractRole role,
        final int increment);
  }

  protected final HashSet<AbstractPrivilegeSet> privilegeSets;
  protected final RoleIdent id;
  protected final DBGGUID gguid;
  protected final URI defaultPageURI;

  /**
   * Constructor for a cumulated role. A cumulated role does not have a specific
   * default page.
   * 
   * @param id
   *          Identifier of the cumulated role.
   */
  public AbstractRole(final RoleIdent id) {
    this(id, null, null);
  }

  public AbstractRole(final RoleIdent id,
      final DBGGUID gguid, final URI defaultPageURI) {
    if (id == null)
      throw new NullPointerException("id");
    this.id = id;
    this.gguid = gguid;
    this.defaultPageURI = defaultPageURI;
    privilegeSets = new HashSet<AbstractPrivilegeSet>();
  }

  public RoleIdent getId() {
    return id;
  }

  public DBGGUID getGGUID() {
    return gguid;
  }

  public abstract AbstractPrivilegeSet lookupPrivilegeSet(
      final PrivilegeSetIdent ident);

  public abstract void clear();

  public boolean containsPrivilegeSet(final AbstractPrivilegeSet privilegeSet) {
    return privilegeSets.contains(privilegeSet);
  }

  public void addPrivilegeSetsFromRole(final AbstractRole role) {
    for (AbstractPrivilegeSet privilegeSet : role.privilegeSets) {
      if (!containsPrivilegeSet(privilegeSet))
        addPrivilegeSet(privilegeSet);
    }
  }

  public URI getDefaultPageURI() {
    return defaultPageURI;
  }

  public void addPrivilegeSet(final AbstractPrivilegeSet privilegeSet) {
    if (!containsPrivilegeSet(privilegeSet)) {
      privilegeSet.updateRoleForThisPrivilege(this, 1);
      privilegeSets.add(privilegeSet);
    } else {
      logger.warn("Warning: Role " + id.toSource()
          + " already contains privilege set " + privilegeSet.getId() + "");
    }
  }

  public void removePrivilegeSet(final Role.PrivilegeSet privilegeSet) {
    if (containsPrivilegeSet(privilegeSet)) {
      privilegeSets.remove(privilegeSet);
      privilegeSet.updateRoleForThisPrivilege(this, -1);
    } else {
      logger.warn("Warning: Role \"" + id.toSource()
          + "\" does not contain a privilege set named \""
          + privilegeSet.getId() + "\"");
    }
  }

  private String privilegeSetsToString() {
    final StringBuffer s = new StringBuffer();
    for (final AbstractPrivilegeSet privilegeSet : privilegeSets) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(privilegeSet.getId().toString());
    }
    return "{" + s + "}";
  }

  public boolean equals(final Object obj) {
    final boolean result;
    if (obj instanceof AbstractRole) {
      final AbstractRole other = (AbstractRole) obj;
      if (id != null) {
        result = id.equals(other.id);
      } else {
        result = other.id == null;
      }
    } else {
      result = false;
    }
    return result;
  }

  @Override
  public String toString() {
    return "Role[id=" + id.getValue() + ", gguid=" + gguid + ", defaultPage="
        + defaultPageURI + ", privilegeSets=" + privilegeSetsToString() + "]";
  }
}

// Local Variables:
// coding:utf-8
// mode:java
// End:
