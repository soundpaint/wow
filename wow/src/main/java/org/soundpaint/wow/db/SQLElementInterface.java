package org.soundpaint.wow.db;

public interface SQLElementInterface {
  public <A, R> R accept(SQLVisitor<A, R> visitor, A arg);
  public String asSQLString();
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
