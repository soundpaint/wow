package org.soundpaint.wow.auth;

import org.soundpaint.wow.db.types.DBIdent;

public class Context {
  private final PrivilegeIdent id;
  private final DBIdent childrenKey;

  public Context(final PrivilegeIdent id, final DBIdent childrenKey) {
    if (id == null)
      throw new NullPointerException("id");
    this.id = id;
    this.childrenKey = childrenKey;
  }

  public PrivilegeIdent getId() {
    return id;
  }

  public DBIdent getChildrenKey() {
    return childrenKey;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
