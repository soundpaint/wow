package org.soundpaint.wow.utils;

public abstract class Ident implements Comparable<Ident> {
  private String value;

  private Ident() {
  }

  protected Ident(final String value) {
    this();
    accept(value);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  protected abstract void accept(final String value) throws ParseException;

  public String toSource() {
    return value;
  }

  public int compareTo(final Ident other) {
    return value.compareTo(other.value);
  }

  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(final Object obj) {
    final boolean result;
    if (!(obj instanceof Ident)) {
      result = false;
    } else {
      final Ident other = (Ident) obj;
      result = value.equals(other.value);
    }
    return result;
  }

  public String toString() {
    return toSource();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
