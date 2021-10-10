package org.soundpaint.wow.form;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.utils.CSSClassIdent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface FormDataValue {
  public String getTypeDisplayName();

  public void setEditable(boolean editable);

  public boolean isEditable();

  public void setCSSClassId(CSSClassIdent cssClassId);

  public CSSClassIdent getCSSClassId();

  public String getUnparsedFormInputData();

  public Node renderAsXHTML(I18N i18n, Document nodeFactory);

  public URL appendToURL(URL url, ParameterName paramName)
      throws MalformedURLException, URISyntaxException;
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
