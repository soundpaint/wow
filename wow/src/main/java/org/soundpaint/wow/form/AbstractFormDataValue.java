package org.soundpaint.wow.form;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.db.SQLElement;
import org.soundpaint.wow.db.SQLVisitor;
import org.soundpaint.wow.db.types.DBIdent;
import org.soundpaint.wow.db.types.DBValue;
import org.soundpaint.wow.utils.CSSClassIdent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractFormDataValue extends SQLElement
    implements DBValue, FormDataValue {
  private boolean editable;
  private DBIdent dbColumnId;
  private CSSClassIdent cssClassId;
  private String unparsedFormInputData;

  private AbstractFormDataValue() {
  }

  public <A, R> R accept(final SQLVisitor<A, R> visitor, final A arg) {
    return visitor.visit(this, arg);
  }

  public AbstractFormDataValue(final String unparsedFormInputData) {
    this();
    this.unparsedFormInputData = unparsedFormInputData;
  }

  public void setEditable(final boolean editable) {
    this.editable = editable;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setDBColumnId(final DBIdent dbColumnId) {
    this.dbColumnId = dbColumnId;
  }

  public DBIdent getDBColumnId() {
    return dbColumnId;
  }

  public void setCSSClassId(final CSSClassIdent cssClassId) {
    this.cssClassId = cssClassId;
  }

  public CSSClassIdent getCSSClassId() {
    return cssClassId;
  }

  public String getUnparsedFormInputData() {
    return unparsedFormInputData;
  }

  public abstract String getTypeDisplayName();

  public abstract boolean hasValidValue();

  public abstract Node
      renderAsXHTML(final I18N i18n, final Document nodeFactory);

  public abstract String renderAsFormInputData(final I18N i18n);

  public abstract DBValue renderAsSQL();

  public URL appendToURL(final URL url, final ParameterName paramName)
      throws MalformedURLException, URISyntaxException {
    final URI originalURI = url.toURI();
    final String originalQuery = originalURI.getQuery();
    final String query =
        originalQuery + (originalQuery.length() > 0 ? "&" : "")
            + paramName.toString() + "=" + unparsedFormInputData;
    final URI uri =
        new URI(originalURI.getScheme(), originalURI.getAuthority(),
            originalURI.getPath(), query, originalURI.getFragment());
    return uri.toURL();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
