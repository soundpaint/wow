package org.soundpaint.wow.db.types;

import org.soundpaint.wow.db.SQLElement;
import org.soundpaint.wow.db.SQLVisitor;

public class StringLiteral extends SQLElement implements StringExpression {
  private final String value;

  private StringLiteral(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public <A, R> R accept(final SQLVisitor<A, R> visitor, final A arg) {
    return visitor.visit(this, arg);
  }

  public static StringLiteral create(final String value) {
    final StringLiteral literal = new StringLiteral(value);
    return literal;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
