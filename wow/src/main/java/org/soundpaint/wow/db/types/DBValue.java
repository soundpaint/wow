package org.soundpaint.wow.db.types;

import org.soundpaint.wow.db.SQLVisitor;

public interface DBValue {
  public <A, R> R accept(SQLVisitor<A, R> visitor, A arg);
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
