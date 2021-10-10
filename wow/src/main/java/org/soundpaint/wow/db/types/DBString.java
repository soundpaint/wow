package org.soundpaint.wow.db.types;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.form.AbstractFormDataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DBString extends AbstractFormDataValue {
  private static final String ID_STRING = "String";
  private static final DBString NULL = new DBString(null, null);

  private String value;

  private DBString(final String value, final String unparsedFormInputData) {
    super(unparsedFormInputData);
    this.value = value;
  }

  public String getTypeDisplayName() {
    return ID_STRING;
  }

  public boolean hasValidValue() {
    return value != null;
  }

  public Node renderAsXHTML(final I18N i18n, final Document nodeFactory) {
    if (value == null)
      throw new InternalError("no data available for rendering");
    return nodeFactory.createTextNode(toString(i18n));
  }

  public String renderAsFormInputData(final I18N i18n) {
    if (value == null)
      throw new InternalError("no data available for rendering");
    return toString(i18n);
  }

  public StringLiteral renderAsSQL() {
    if (value == null)
      throw new InternalError("no data available for rendering");
    return StringLiteral.create(value);
  }

  public static DBString fromString(final String value) {
    if (value != null) {
      return new DBString(value, null);
    } else {
      return NULL;
    }
  }

  public static DBString parseLocalized(final I18N i18n, final String strValue) {
    // no localization for string values
    return parseSQL(strValue);
  }

  public static DBString parseSQL(final String strValue) {
    if (strValue != null) {
      return new DBString(strValue, strValue);
    } else {
      return NULL;
    }
  }

  public int length() {
    if (value == null)
      throw new InternalError("no data available");
    return value.length();
  }

  public String getValue() {
    if (value == null)
      throw new InternalError("no data available");
    return value;
  }

  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(final Object obj) {
    if (obj instanceof DBString) {
      return value.equals(((DBString) obj).value);
    } else {
      return false;
    }
  }

  public String toString(final I18N i18n) {
    // no localization for string values
    return toString();
  }

  public String toString() {
    return value != null ? value : "";
  }
}

// Local Variables:
// coding:utf-8
// mode:java
// End:
