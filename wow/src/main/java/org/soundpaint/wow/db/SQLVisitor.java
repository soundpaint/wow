package org.soundpaint.wow.db;

import org.soundpaint.wow.db.types.DBValue;

public interface SQLVisitor<A, R> {
  public R visit(DBValue value, A arg);
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
