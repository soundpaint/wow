package org.soundpaint.wow.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Config {

  private URI fsRoot;
  private Element configNode;
  private String log4jConfigFilePath;

  private Config() {
  }

  protected Config(final Document document, final URI fsRoot) throws IOException {
    this();
    this.fsRoot = fsRoot;
    configNode = document.getDocumentElement();
    if (!configNode.getTagName().equals("config"))
      throw new ParseException("bad document root element: \""
          + configNode.getTagName() + "\" (expected: \"config\")");

    /*-- Log4J --*/
    final Element log4jConfigurationNode = getPropertyElement("log4j-configuration");
    final URI log4jConfigFileURI =
        fsRoot.resolve(log4jConfigurationNode.getAttribute("file-path"));
    log4jConfigFilePath = new File(log4jConfigFileURI).getCanonicalPath();
    System.setProperty("log4j.configuration", log4jConfigFilePath);
  }

  protected Element getPropertyElement(final String name) {
    final NodeList elements = configNode.getElementsByTagName(name);
    if (elements.getLength() > 1) { throw new ParseException(
        "config file contains multiple definitions for property " + name); }
    if (elements.getLength() < 1) { throw new ParseException(
        "config file is missing definition for property " + name); }
    final Node propertyNode = elements.item(0);
    if (!(propertyNode instanceof Element)) { throw new ParseException(
        "config node " + name + " is not an element"); }
    return (Element) propertyNode;
  }

  public URI getFileSystemRoot() {
    return fsRoot;
  }

  public String getLog4jConfigFilePath() {
    return log4jConfigFilePath;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
