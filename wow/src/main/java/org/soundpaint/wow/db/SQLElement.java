package org.soundpaint.wow.db;

public abstract class SQLElement
  implements SQLElementInterface
{
  public abstract <A, R> R accept(SQLVisitor<A, R> visitor, A arg);

  public String asSQLString() {
    return accept(SQLPrinter.getDefaultInstance(), null);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
