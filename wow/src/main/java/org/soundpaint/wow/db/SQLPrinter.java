package org.soundpaint.wow.db;

import org.soundpaint.wow.db.types.DBValue;

public class SQLPrinter implements SQLVisitor<Void, String> {
  private static final SQLPrinter DEFAULT_INSTANCE = new SQLPrinter();

  public static SQLPrinter getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @Override
  public String visit(final DBValue value, final Void _void) {
    return value.toString();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
