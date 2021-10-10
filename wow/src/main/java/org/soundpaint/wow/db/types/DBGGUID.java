package org.soundpaint.wow.db.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.form.AbstractFormDataValue;
import org.soundpaint.wow.utils.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DBGGUID extends AbstractFormDataValue {
  // TODO: This class partially supports uninitialized GGUID values
  // (value == null), but does not support initializing an
  // uninitialized GGUID. Maybe there is no need for uninitialized
  // GGUIDs? If there is, a single constant uninitialized GGUID may
  // suffice.

  private static final String ID_GGUID = "GGUID";
  public static final DBGGUID NULL = new DBGGUID(null, null);

  public static String asCSV(
      final Collection<DBGGUID> gguids) {
    final StringBuffer csv = new StringBuffer();
    for (final DBGGUID gguid : gguids) {
      if (gguid == null) { throw new IllegalArgumentException(
          "null GGUID in GGUID array"); }
      final String gguidStr = gguid.getValue();
      if (gguidStr == null) { throw new IllegalArgumentException(
          "GGUID null value in GGUID array"); }
      if (csv.length() > 0)
        csv.append(",");
      // TODO: SQL encode gguidStr.
      csv.append("\"" + gguidStr + "\"");
    }
    return csv.toString();
  }

  public static DBGGUID create() {
    final String unparsedFormInputData = UUID.randomUUID().toString();
    return parseSQL(unparsedFormInputData);
  }

  private String value;

  private DBGGUID(final String value, final String unparsedFormInputData) {
    super(unparsedFormInputData);
    this.value = value;
  }

  public String getTypeDisplayName() {
    return ID_GGUID;
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

  private static boolean isValidGGUIDChar(final char ch) {
    return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')
        || (ch >= 'a' && ch <= 'z') || (ch == '.') || (ch == '-')
        || (ch == '_');
  }

  private static void checkSyntax(final String strValue) {
    for (int i = 0; i < strValue.length(); i++) {
      final char ch = strValue.charAt(i);
      if (!isValidGGUIDChar(ch))
        throw new ParseException("invalid GGUID syntax");
    }
  }

  public static DBGGUID parseLocalized(final I18N i18n,
      final String strValue) {
    // no localization for GGUID values
    return parseSQL(strValue);
  }

  public static DBGGUID parseSQL(final String strValue) {
    if ((strValue != null) && (strValue.length() > 0)) {
      checkSyntax(strValue);
      return new DBGGUID(strValue, null);
    } else {
      return getNullGGUID();
    }
  }

  private static DBGGUID getNullGGUID() {
    return NULL;
  }

  public static Collection<DBGGUID> parseSQL(
      final String[] strValues) {
    final List<DBGGUID> result = new ArrayList<DBGGUID>();
    for (final String strValue : strValues) {
      result.add(DBGGUID.parseSQL(strValue));
    }
    return result;
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
    if (obj instanceof DBGGUID) {
      return value.equals(((DBGGUID) obj).value);
    } else {
      return false;
    }
  }

  public String toString(final I18N i18n) {
    // no localization for GGUID values
    return toString();
  }

  public String toString() {
    return value != null ? value : "";
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
