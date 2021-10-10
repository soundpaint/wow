package org.soundpaint.wow;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.auth.Role;
import org.soundpaint.wow.auth.RoleIdent;
import org.soundpaint.wow.auth.User;
import org.soundpaint.wow.auth.UsersConfig;
import org.soundpaint.wow.db.types.DBString;

public class Session implements HttpSessionBindingListener {
  private static final Logger logger = LogManager.getLogger(Session.class);

  private static final HashMap<HttpSession, Session> lookupTable =
      new HashMap<HttpSession, Session>();

  final public static Session reuseOrCreate(final HttpSession httpSession) {
    final Session session = lookupTable.get(httpSession);
    final Session checkedSession;
    if (session != null) {
      checkedSession = session;
    } else {
      checkedSession = new Session(httpSession);
    }
    return checkedSession;
  }

  private HttpSession httpSession;
  private I18N i18n;
  private User user;
  private Role cumulatedRole;
  private Set<Role> activeRoles;
  private DBString loginName;

  private Session(final HttpSession httpSession) {
    this.httpSession = httpSession;
    httpSession.setAttribute(httpSession.getId(), this);
    resetSessionVariables();
  }

  private void resetSessionVariables() {
    // let subclass reset all of its member variables
    resetUserSessionVariables();
    i18n = I18N.create();
    user = null;
    cumulatedRole =
        RuntimeConfig.getDefaultInstance().getRolesConfig().getDefaultRole();
    activeRoles = new HashSet<Role>();
    activeRoles.add(cumulatedRole);
    loginName = null;
  }

  private void invalidate() {
    httpSession.setAttribute(httpSession.getId(), null);
    httpSession.invalidate();
    httpSession = null;
    resetSessionVariables();
  }

  /**
   * Override this method to release resources / reset member variables upon
   * exiting a session. Note: To avoid security holes, any confidential session
   * information MUST be cleared / reset when invalidating the session.
   */
  protected void resetUserSessionVariables() {
    // default implementation does nothing
  }

  final public void logout() {
    invalidate();
  }

  private RoleIdent createIdent(final String s) {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      final char ch = s.charAt(i);
      if (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z'))
          || ((ch >= '0') && (ch <= '9')) || (ch == '_')) {
        sb.append(ch);
      } else {
        sb.append('_');
      }
    }
    return new RoleIdent(sb.toString());
  }

  final public boolean login(final Connection connection,
      final DBString loginName, final DBString password) throws IOException {
    if (isLoggedIn())
      logout();
    if ((loginName == null) || (password == null))
      return false;
    final UsersConfig usersConfig =
        RuntimeConfig.getDefaultInstance().getUsersConfig();
    final User user = usersConfig.lookupUser(connection, loginName.getValue());
    if (user == null) {
      logger.warn("login: no such user: " + loginName.getValue());
      return false;
    }
    if (!user.canAuthenticate(password.getValue())) {
      logger.warn("login: authentication failed for user " + loginName);
      return false;
    }
    this.loginName = loginName;
    this.user = user;
    cumulateRoles(user.getRoles(), createIdent("Cumulated Role for "
        + loginName.getValue()));
    // TODO: Alternatively to roles accumulation, let the user
    // explicitly select one of the roles.
    return true;
  }

  private void cumulateRoles(final Collection<Role> roles, final RoleIdent id) {
    activeRoles.clear();
    cumulatedRole = new Role(id);
    for (final Role role : roles) {
      activeRoles.add(role);
      cumulatedRole.addPrivilegeSetsFromRole(role);
    }
  }

  final public long getCreationTime() {
    return httpSession.getCreationTime();
  }

  final public String getId() {
    return httpSession.getId();
  }

  final public long getLastAccessedTime() {
    return httpSession.getLastAccessedTime();
  }

  final public int getMaxInactiveInterval() {
    return httpSession.getMaxInactiveInterval();
  }

  final public boolean isNew() {
    return httpSession.isNew();
  }

  final public void setMaxInactiveInterval(final int interval) {
    httpSession.setMaxInactiveInterval(interval);
  }

  @Override
  final public void valueBound(final HttpSessionBindingEvent event) {
    lookupTable.put(event.getSession(), this);
  }

  @Override
  final public void valueUnbound(final HttpSessionBindingEvent event) {
    lookupTable.remove(this);
  }

  final public I18N getI18N() {
    return i18n;
  }

  final public User getUser() {
    return user;
  }

  private static final Role[] EMPTY_ROLE_ARRAY = new Role[0];

  final public Role[] getActiveRoles() {
    return activeRoles.toArray(EMPTY_ROLE_ARRAY);
  }

  final public Role getCumulatedRole() {
    return cumulatedRole;
  }

  final public DBString getLoginName() {
    return loginName;
  }

  final public boolean isLoggedIn() {
    return loginName != null;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
