package org.soundpaint.wow.auth;

public interface Privilege {
  public boolean isGrantedForRole(final Role role);
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
