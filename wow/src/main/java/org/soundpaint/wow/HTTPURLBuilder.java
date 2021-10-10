package org.soundpaint.wow;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.soundpaint.wow.utils.ParseException;

public class HTTPURLBuilder {

  public static enum HTTPScheme {
    HTTP, HTTPS;

    private HTTPScheme() {
    }

    private static HTTPScheme parse(final String scheme) {
      if (scheme.equalsIgnoreCase("http")) {
        return HTTP;
      } else if (scheme.equalsIgnoreCase("https")) {
        return HTTPS;
      } else {
        throw new ParseException("invalid http scheme: " + scheme);
      }
    }

    public String toString() {
      switch (this) {
      case HTTP:
        return "http";
      case HTTPS:
        return "https";
      default:
        throw new InternalError("unexpected case fall through");
      }
    }
  }

  private HTTPScheme scheme;
  private String host;
  private int port;
  private String path, fragment;
  private HashMap<String, Vector<String>> queryParameters;

  private static final RuntimeConfig RUNTIME_CONFIG = RuntimeConfig
      .getDefaultInstance();

  private static String encodeSpecial(final String unencoded) {
    final StringBuffer encoded = new StringBuffer();
    for (int i = 0; i < unencoded.length(); i++) {
      final char ch = unencoded.charAt(i);
      switch (ch) {
      case ' ':
        encoded.append("%20");
        break;
      case '#':
        encoded.append("%23");
        break;
      case '%':
        encoded.append("%25");
        break;
      case '&':
        encoded.append("%26");
        break;
      case ',':
        encoded.append("%2C");
        break;
      case '/':
        encoded.append("%2F");
        break;
      case ';':
        encoded.append("%3B");
        break;
      case '=':
        encoded.append("%3D");
        break;
      case '?':
        encoded.append("%3F");
        break;
      default:
        encoded.append(ch);
      }
    }
    return encoded.toString();
  }

  private static void checkPath(final String path) {
    if (path == null)
      throw new NullPointerException("path");
    // TODO: Validate path syntax.
  }

  private static void checkFragment(final String fragment) {
    // no null-check for optional fragment
    // TODO: Check syntax of fragment, if provided.
  }

  private HTTPURLBuilder() {
    queryParameters = new HashMap<String, Vector<String>>();
  }

  /**
   * Creates an absolute HTTP URL for the current host and port.
   */
  public HTTPURLBuilder(final String path) {
    this(path, null);
  }

  /**
   * Creates an absolute HTTP URL for the current host and port.
   */
  public HTTPURLBuilder(final String path, final String fragment) {
    this();
    checkPath(path);
    checkFragment(fragment);
    this.scheme = null;
    this.host = null;
    this.port = -1;
    this.path = path;
    this.fragment = fragment;
  }

  /**
   * Creates an absolute HTTP URL from the given java.net.URI object.
   */
  public static HTTPURLBuilder fromURI(final URI uri) {
    final HTTPURLBuilder httpURLBuilder = new HTTPURLBuilder();
    final String scheme = uri.getScheme();
    if (scheme != null) {
      // use scheme provided with URI
      httpURLBuilder.scheme = HTTPScheme.parse(uri.getScheme());
    } else {
      // use HTTP as default scheme
      httpURLBuilder.scheme = HTTPScheme.HTTP;
    }
    httpURLBuilder.host = uri.getHost();
    httpURLBuilder.port = uri.getPort();
    httpURLBuilder.path = uri.getPath();
    httpURLBuilder.fragment = uri.getFragment();
    httpURLBuilder.addQueryParametersFromQueryString(uri.getQuery());
    return httpURLBuilder;
  }

  public static HTTPURLBuilder relativeToWebRoot(final String path) {
    final URI webRoot = RUNTIME_CONFIG.getWebRoot();
    final URI uri = webRoot.resolve(path);
    return fromURI(uri);
  }

  public void clearQueryParameter(final String name) {
    if (name == null)
      throw new NullPointerException("name");
    final Vector<String> values = queryParameters.get(name);
    if (values != null) {
      values.clear();
    }
  }

  public void addQueryParameter(final String name, final String value) {
    if (name == null)
      throw new NullPointerException("name");
    if (value == null)
      throw new NullPointerException("value");
    // TODO: Check that there are no special characters in the name or value
    // String (not solely, but also in order to prevent injection attacks).
    final Vector<String> existingValues = queryParameters.get(name);
    final Vector<String> values;
    if (existingValues == null) {
      values = new Vector<String>();
      queryParameters.put(name, values);
    } else {
      values = existingValues;
    }
    values.add(value);
  }

  /**
   * @param keyValuePair
   *          A key-value pair in the form &lt;key&gt; "=" &lt;value&gt;.
   */
  public void addQueryParameter(final String keyValuePair) {
    final int eqPos = keyValuePair.indexOf('=');
    if (eqPos >= 0) {
      final String key = keyValuePair.substring(0, eqPos);
      final String value =
          keyValuePair.substring(eqPos + 1, keyValuePair.length());
      addQueryParameter(key, value);
    } else {
      throw new ParseException("bad URL parameter key-value pair: "
          + keyValuePair);
    }
  }

  public void addQueryParameters(final Map<String, String[]> parameterMap) {
    if (parameterMap != null) {
      for (final String parameterName : parameterMap.keySet()) {
        for (final String parameterValue : parameterMap.get(parameterName)) {
          addQueryParameter(parameterName, parameterValue);
        }
      }
    }
  }

  // TODO: We should deploy those classes that implement the
  // org.poly.ParameterParser class to parse the query. However, this approach
  // requires knowledge about the parameters' types. So, for now, we just
  // parse String parameters.
  public void addQueryParametersFromQueryString(final String query) {
    if ((query == null) || query.isEmpty()) {
      // empty query => nothing to do
      return;
    }
    int prevAmpPos;
    int ampPos = -1;
    do {
      prevAmpPos = ampPos;
      ampPos = query.indexOf('&', ampPos);
      if (ampPos > 0) {
        final String keyValuePair = query.substring(prevAmpPos + 1, ampPos);
        addQueryParameter(keyValuePair);
      }
    } while (ampPos > 0);
    final String keyValuePair = query.substring(prevAmpPos + 1);
    addQueryParameter(keyValuePair);
  }

  private String buildQuery() {
    final String query;
    if (queryParameters.size() > 0) {
      final StringBuffer s = new StringBuffer();
      for (final String name : queryParameters.keySet()) {
        final Vector<String> values = queryParameters.get(name);
        for (final String value : values) {
          if (s.length() > 0) {
            s.append("&");
          }
          s.append(encodeSpecial(name) + "=" + encodeSpecial(value));
        }
      }
      query = s.toString();
    } else {
      query = null;
    }
    return query;
  }

  public URI toURI() throws URISyntaxException {
    return new URI(scheme != null ? scheme.toString() : null, null, host, port,
        path, buildQuery(), fragment);
  }

  public String toString() {
    final StringBuffer s = new StringBuffer();
    if (scheme != null) {
      s.append(scheme);
      s.append(":");
    }
    if (host != null) {
      s.append("//");
      s.append(host);
    }
    if (port > 0) {
      s.append(":");
      s.append(port);
    }
    if (path != null) {
      s.append(path);
    }
    final String query = buildQuery();
    if (query != null) {
      s.append("?");
      s.append(query);
    }
    if ((fragment != null) && (fragment.length() > 0)) {
      s.append("#");
      s.append(fragment);
    }
    return s.toString();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
