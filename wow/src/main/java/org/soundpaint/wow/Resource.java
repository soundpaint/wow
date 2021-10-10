package org.soundpaint.wow;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.form.AbstractFormDataValue;
import org.soundpaint.wow.form.FormDataParameter;
import org.soundpaint.wow.i18n.Language;
import org.soundpaint.wow.utils.ParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public abstract class Resource
{
  private static final Logger logger = LogManager.getLogger(Resource.class);

  private static Charset iso8859_1;
  private static Charset utf8;

  static
  {
    try {
      iso8859_1 = Charset.forName("ISO-8859-1");
    } catch (final Exception e) {
      iso8859_1 = null;
      logger.error("no ISO-8859-1 encoding support found", e);
    }
    try {
      utf8 = Charset.forName("UTF-8");
    } catch (final Exception e) {
      utf8 = null;
      logger.error("no UTF-8 encoding support found", e);
    }
  }

  /**
   * If a ISO-8859-1 encoded byte sequence of characters was erroneously assumed
   * to be UTF-8 encoded, then call this method to correct the badly converted
   * String.
   * 
   * @param miscoded
   *          A broken String with badly encoded characters.
   * @return The fixed String with a corrected sequence of characters.
   */
  private static String recodeISOLatin1ToUTF8(final String miscoded)
  {
    final CharBuffer data = utf8.decode(ByteBuffer.wrap(miscoded.getBytes()));
    final ByteBuffer outputBuffer = iso8859_1.encode(data);
    return new String(outputBuffer.array());
  }

  protected static final URI resolveAgainstWebRoot(final URI uri)
  {
    if (uri.toString().startsWith("/")) {
      logger.warn("trying to resolve absolute URI " +
                  "=> effectively ignoring web root URI");
    }
    final URI webRoot = RuntimeConfig.getDefaultInstance().getWebRoot();
    return webRoot.resolve(uri);
  }

  protected static final URI getURI(final String pathRelativeToWebRoot)
  {
    // TODO: Handle exception in the case of an invalid relative path.
    final URI uriRelativeToWebRoot = URI.create(pathRelativeToWebRoot);
    return resolveAgainstWebRoot(uriRelativeToWebRoot);
  }

  /**
   * @deprecated Rather than specifying a relative path as String, we should
   *             instead specify a Resource and get the path from its navigator
   *             entry.
   */
  @Deprecated
  protected static final HTTPURLBuilder createLinkBeneathRoot(
      final String relativePath)
  {
    final HTTPURLBuilder httpURLBuilder =
      HTTPURLBuilder.relativeToWebRoot(relativePath);
    return httpURLBuilder;
  }

  protected static void addToFormDataParameterList(
    final List<FormDataParameter> formDataParameterList,
    final String name,
    final Object data,
    final int arrayDim)
  {
    if (arrayDim > 0) {
      Object[] dataArray = (Object[]) data;
      for (int i = 0; i < dataArray.length; i++) {
        addToFormDataParameterList(formDataParameterList, name + "." + (i + 1),
            dataArray, arrayDim - 1);
      }
    } else {
      AbstractFormDataValue value = (AbstractFormDataValue) data;
      formDataParameterList.add(new FormDataParameter(name, value));
    }
  }

  protected static ResponseBody redirectHereInternal(
    final ResourceContext context,
    final String navigationPath,
    final FormDataParameter... params)
    throws ServletException
  {
    final StringBuffer query = new StringBuffer();
    for (final FormDataParameter param : params) {
      if (query.length() > 0) {
        query.append("&");
      }
      final String name = param.getName();
      query.append(name);
      query.append("=");
      final AbstractFormDataValue value = param.getValue();
      // TODO: Escape parameter value, if necessary.
      query.append(value.toString());
    }
    final String uriStr;
    if (params.length > 0) {
      uriStr = navigationPath + "?" + query;
    } else {
      uriStr = navigationPath;
    }
    try {
      context.getResponse().sendRedirect(uriStr);
      context.setRedirected(true);
    } catch (final IOException e) {
      throw new ServletException("redirect failed", e);
    }
    return null;
  }

  protected static final boolean isFirstLanguage(final int languageIndex)
  {
    return languageIndex == 0;
  }

  protected static final String getLanguageKey(final int languageIndex)
  {
    return Language.fromOrdinal(languageIndex).getKey();
  }

  protected static final String renderValue(final AbstractFormDataValue value)
  {
    return value != null ? value.toString() : "";
  }

  protected static final String getCSSURI()
  {
    return RuntimeConfig.getDefaultInstance().getCSSURI().toString();
  }

  protected static final String getShortcutIconURI()
  {
    return RuntimeConfig.getDefaultInstance().getShortcutIconURI().toString();
  }

  private final ResourceContext context;

  private Resource() { throw new UnsupportedOperationException(); }

  protected Resource(final ResourceContext context)
  {
    this.context = context;
  }

  protected ResourceContext getContext()
  {
    return context;
  }

  protected abstract String getDefaultNavigationPath();

  private static void parseParameter(
    final Map<String, ParameterParser> parameterParsers,
    final String parameterName,
    final File parameterValue)
  {
    // TODO
    parameterValue.delete();
  }

  private void parseParameter(
    final Map<String, ParameterParser> parameterParsers,
    final String key,
    final String[] values)
    throws ParseException, IOException, ServletException
  {
    // FIXME: A dot may also be an ordinary character of a double value or a
    // String. It is invalid to assume that the dot character always introduces
    // an indexed parameter.
    final int dotPos = key.indexOf('.');
    final String keyPrefix, keySuffix;
    if (dotPos > 0) {
      keyPrefix = key.substring(0, dotPos);
      keySuffix = key.substring(dotPos + 1);
    } else {
      keyPrefix = key;
      keySuffix = "";
    }
    final ParameterParser parser = parameterParsers.get(keyPrefix);
    if (parser == null)
      throw new IOException("unexpected page parameter: \"" + key + "\"");
    if (context.getEncoding() == ResourceContext.Encoding.ISO_LATIN_1) {
      for (int i = 0; i < values.length; i++) {
        values[i] = recodeISOLatin1ToUTF8(values[i]);
      }
    } else if (context.getEncoding() == ResourceContext.Encoding.UTF8) {
      // we already have UTF-8 characters => nothing to do
    } else {
      logger.warn("failed determining character set encoding of form data");
      // Continue and hope that not too many characters are affected
      // from bad encoding.
    }
    assert values != null : "unexpected empty parameter: \"" + key + "\"";
    parser.parse(this, keyPrefix, keySuffix, values);
  }

  protected void parseParameters(final Map<String, ParameterParser> parameterParsers)
    throws ServletException, IOException, ParseException
  {
    final HttpServletRequest request = context.getRequest();
    final Map<String, String[]> parameterMap = request.getParameterMap();
    for (final String parameterName : parameterMap.keySet()) {
      // Retrieves <input type="text" name="${name}">
      final String parameterValue = request.getParameter(parameterName);
      parseParameter(parameterParsers, parameterName, new String[] { parameterValue });
      // FIXME: For now, we do not support multiple value parameters
      // here, i.e. we assume that the String[] array always has a
      // constant length of 1.
    }
    final List<Part> fileParts =
      request.getParts().stream().
      filter(part -> part.getSize() > 0).collect(Collectors.toList());
    for (final Part filePart : fileParts) {
      // <input type="file" name="${name}" />
      //final String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
      final String parameterName = filePart.getName();
      //final InputStream fileStream = filePart.getInputStream();
      final File tmpFile = File.createTempFile("wow", "upload");
      filePart.write(tmpFile.getCanonicalPath());
      parseParameter(parameterParsers, parameterName, tmpFile);
    }
    for (final ParameterParser parser : parameterParsers.values()) {
      parser.finishParse(this);
    }
  }

  /**
   * This method is the first of all resource-specific methods that gets called.
   * The implementation of this method is automatically provided by the template
   * compiler as part of the resource stub. If no resource stub is generated,
   * the user must provide an implementation for this method. This method should
   * parse all parameters that occur in the request URL or form data. If there
   * is an unexpected parameter, the method should log a warning and discard the
   * parameter. If a required parameter is missing, it should throw an
   * exception. If two or more parameters with the same name occur, but the
   * parameter with that name is expected to be passed in exactly once, this
   * method should throw an exception.
   */
  public abstract void parseParameters()
    throws ParseException, IOException, ServletException;

  protected void checkLanguageChangeRequest() throws InvalidURLException
  {
    final I18N i18n = context.getI18N();
    final HttpServletRequest request = getContext().getRequest();
    final String[] langValues = request.getParameterValues("lang");
    if (langValues != null) {
      if (langValues.length != 1) {
        throw new InvalidURLException("invalid URL: " +
                                      "multiple parameter \"lang\"", i18n);
      }
      final String languageKey = langValues[0];
      final Language language;
      try {
        logger.debug("selected language=" + languageKey);
        language = Language.fromKey(languageKey);
      } catch (final IllegalArgumentException e) {
        throw new InvalidURLException("invalid URL: unsupported language \"" +
                                      languageKey + "\"", i18n);
      }
      i18n.setPreferredLanguage(language);
      i18n.setLocale(new Locale(language.getKey()));
    }
  }

  protected abstract ResponseBody serveActions()
    throws IOException, ServletException;

  protected abstract ResponseBody createResponseBody()
    throws IOException, InvalidURLException, ServletException;

  public final ResponseBody serve()
    throws ServletException, IOException, InvalidURLException
  {
    logger.debug("using resource handler " + this.getClass());
    logger.debug("parsing parameters");
    final ResourceContext context = getContext();
    context.determineParameterEncoding();
    checkLanguageChangeRequest();
    if (!context.ignoreParameters()) {
      try {
        parseParameters();
      } catch (final ParseException e) {
        throw new IOException("request contains a bad URL or POST parameter", e);
      }
    }
    final ResponseBody actionResponse = serveActions();
    if (actionResponse != null)
      return actionResponse;
    logger.debug("generating XHTML page");
    final ResponseBody responseBody = createResponseBody();
    return responseBody;
  }

  protected final Map<String, String[]> getParameterMap()
  {
    final HttpServletRequest request = context.getRequest();
    final Map<String, String[]> parameterMap =
        (Map<String, String[]>) request.getParameterMap();
    return parameterMap;
  }

  protected final HTTPURLBuilder getSelfHRef()
  {
    final HttpServletRequest request = context.getRequest();
    final URI uri = URI.create(request.getRequestURI());
    return HTTPURLBuilder.fromURI(uri);
  }

  protected final HTTPURLBuilder getLanguageHRef(final int languageIndex)
  {
    final HTTPURLBuilder url = getSelfHRef();
    url.addQueryParameters(getParameterMap());
    url.clearQueryParameter("lang");
    url.addQueryParameter("lang", Language.fromOrdinal(languageIndex)
        .toString());
    return url;
  }

  public void finalize() {}
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
