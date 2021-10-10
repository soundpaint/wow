package org.soundpaint.wow.i18n;

import java.util.Arrays;

public enum Language
{
  EN("en"), DE("de");

  private final String key;

  private Language(final String key)
  {
    this.key = key;
  }
  
  public String getKey() { return key; }

  /**
   * @throws <code>IndexOutOfBoundsException</code>, if
   * <code>index</code> is less than 0 or greater than or equal
   * to the number of available languages.
   */
  public static Language fromOrdinal(final int index)
  {
    return Language.values()[index];
  }

  /**
   * @throws <code>NoSuchElementException</code>, if there is no
   * language that corresponds to the specified key.
   */
  public static Language fromKey(final String key)
  {
    return Arrays.stream(Language.values()).
      filter(lang -> lang.key == key).findFirst().get();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
