package org.soundpaint.wow;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.auth.Role;
import org.soundpaint.wow.auth.User;
import org.soundpaint.wow.navigator.AbstractNavigator.Entry;
import org.soundpaint.wow.utils.Messages;

public class ResourceContext
{
  private static final Logger logger = LogManager.getLogger(Resource.class);

  public static enum Encoding
  {
    ISO_LATIN_1, UTF8, UNKNOWN;
  }

  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private boolean ignoreParameters;
  private boolean allowRedirect;
  private boolean redirected;
  private final ServletOutputStream outputStream;
  private final PrintWriter outputWriter;
  private final Session session;

  // TODO: Cache role and privilegedNavigationEntries, such that they do not
  // need computed for each HTTP request over and over again.
  // But pay attention: Privileges of roles may be updated via the web
  // interface. That is, when supporting caching, there must be an option to
  // invalidate the complete cache.
  private final List<Entry> privilegedNavigationEntries;
  private final SystemConfig systemConfig;
  private final AppConfig userProps;
  private final Date now;
  private final Messages messages;
  private Encoding encoding;

  /**
   * 
   * @param request
   * @param response
   * @param ignoreParameters
   *          If <code>true</code>, ignore all HTTP URL parameters and form data
   *          (i.e., method {@link #parseParameters()} will not be called). This
   *          is useful when redirecting to a different page that does not know
   *          about the parameters from the origin page.
   * @throws ServletException
   */
  public ResourceContext(final HttpServletRequest request,
                         final HttpServletResponse response)
    throws ServletException
  {
    this.request = request;
    this.response = response;
    this.ignoreParameters = false;
    allowRedirect = true;
    redirected = false;
    try {
      outputStream = response.getOutputStream();
    } catch (final IOException e) {
      throw new ServletException("failed getting output stream", e);
    }
    this.outputWriter = new PrintWriter(outputStream);
    this.session = Session.reuseOrCreate(request.getSession());
    final RuntimeConfig runtimeConfig = RuntimeConfig.getDefaultInstance();
    this.privilegedNavigationEntries =
      runtimeConfig.getNavigator().
      getPrivilegedNavigationEntries(session.getCumulatedRole());
    systemConfig = RuntimeConfig.getDefaultInstance().getOptionsConfig();
    userProps = AppConfig.getDefault();
    now = new Date();
    messages = new Messages();
  }

  public Session getSession()
  {
    return session;
  }

  public void determineParameterEncoding()
  {
    final String encodingTest = request.getParameter("encodingTest");
    if (encodingTest != null) {
      if (encodingTest.equals("ä"))
        encoding = Encoding.UTF8;
      else if (encodingTest.equals("Ã¤"))
        encoding = Encoding.ISO_LATIN_1;
      else
        encoding = Encoding.UNKNOWN;
      logger.debug("detected form parameter encoding: " + encoding); // DEBUG
    } else {
      encoding = Encoding.UNKNOWN;
    }
  }

  public int getIssuesCount()
  {
    // TODO
    return 3;
  }

  public String getIssueTitle(final int index)
  {
    // TODO
    return "[TODO: Title #" + index + "]";
  }

  public String getIssueTextStart(final int index)
  {
    // TODO
    return "[TODO: Text #" + index + "]";
  }

  // ---- NAVIGATION ----

  // navigation level #1 = menu group
  // navigation level #2 = group entry
  // navigation level #3 = entry tab

  // TODO: Generalize code for arbitrary amount of levels.

  public int getLevel1Count()
  {
    return privilegedNavigationEntries.size();
  }

  private Entry getLevel1Entry(final int level1Index)
  {
    return privilegedNavigationEntries.get(level1Index);
  }

  public String getLevel1Label(final int level1Index, final I18N i18n)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    return level1Entry.getLocalizedLabel(i18n);
  }

  /**
   * Returns the number of navigation menu entries for the current user.
   */
  public int getLevel2Count(final int level1Index)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    // DEBUG START
    if (level1Entry == null) {
      final StringBuffer s = new StringBuffer();
      for (final Entry entry : privilegedNavigationEntries) {
        if (s.length() > 0) {
          s.append(", ");
        }
        s.append(entry.getId());
      }
      final Role role = session.getCumulatedRole();
      throw new NullPointerException("level1Entry is null for level1Index="
          + level1Index + "(privilegedNavigationEntries=[" + s + "], role="
          + role + ")");
    }
    // DEBUG END
    return level1Entry.size();
  }

  public String getLevel2Label(final int level1Index,
                               final int level2Index,
                               final I18N i18n)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    return level2Entry.getLocalizedLabel(i18n);
  }

  public URI getLevel2URI(final int level1Index, final int level2Index)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    return level2Entry.getURI();
  }

  public int getLevel3Count(final int level1Index, final int level2Index)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    return level2Entry.size();
  }

  public String getLevel3Path(final int level1Index,
                              final int level2Index,
                              final int level3Index)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    final Entry level3Entry = level2Entry.getChild(level3Index);
    return level3Entry.getURI().toString();
  }

  public String getLevel3HRef(final int level1Index,
                              final int level2Index,
                              final int level3Index)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    final Entry level3Entry = level2Entry.getChild(level3Index);
    final String pagePath = level3Entry.getURI().toString();
    final HTTPURLBuilder url = new HTTPURLBuilder(pagePath, null);
    url.addQueryParameters((Map<String, String[]>) request.getParameterMap());
    return url.toString();
  }

  public String getLevel3Name(final int level1Index,
                              final int level2Index,
                              final int level3Index,
                              final I18N i18n)
  {
    final Entry level1Entry = getLevel1Entry(level1Index);
    final Entry level2Entry = level1Entry.getChild(level2Index);
    final Entry level3Entry = level2Entry.getChild(level3Index);
    return level3Entry.getLocalizedLabel(i18n);
  }

  public boolean isMe(final URI user)
  {
    final boolean isMe;
    final Session session = getSession();
    if (user != null) {
      isMe = user.equals(session.getUser().getUserGGUID());
    } else {
      // Special case (user == null), representing user 'admin'. Still need to
      // check if I am really that user.
      isMe = session.getLoginName().getValue().equals(User.USERNAME_OF_ROOT);
    }
    return isMe;
  }

  public final boolean isLoggedIn()
  {
    return getSession().isLoggedIn();
  }

  public final int getLanguagesCount()
  {
    return getI18N().getLanguagesCount();
  }

  public HttpServletRequest getRequest()
  {
    return request;
  }

  public HttpServletResponse getResponse()
  {
    return response;
  }

  public boolean ignoreParameters()
  {
    return ignoreParameters;
  }

  public void setIgnoreParameters(final boolean ignoreParameters)
  {
    this.ignoreParameters = ignoreParameters;
  }

  public boolean isAllowRedirect()
  {
    return allowRedirect;
  }

  public void setAllowRedirect(final boolean allowRedirect)
  {
    this.allowRedirect = allowRedirect;
  }

  public boolean isRedirected()
  {
    return redirected;
  }

  public void setRedirected(final boolean redirected)
  {
    this.redirected = redirected;
  }

  public ServletOutputStream getOutputStream()
  {
    return outputStream;
  }

  public PrintWriter getWriter()
  {
    return outputWriter;
  }

  public AppConfig getUserProps()
  {
    return userProps;
  }

  public Date getNow()
  {
    return now;
  }
  
  public Messages getMessages()
  {
    return messages;
  }
  
  public Encoding getEncoding()
  {
    return encoding;
  }

  // convenience methods
  
  public final I18N getI18N()
  {
    return getSession().getI18N();
  }

  public final void addInfo(final Object key, final String message)
  {
    getMessages().add(Messages.Severity.INFO, key, message);
  }

  public final void addWarning(final Object key, final String message)
  {
    getMessages().add(Messages.Severity.WARNING, key, message);
  }

  public final void addError(final Object key, final String message)
  {
    getMessages().add(Messages.Severity.ERROR, key, message);
  }

  public final boolean haveInfo(final Object key)
  {
    return getMessages().have(Messages.Severity.INFO, key);
  }

  public final boolean haveWarning(final Object key)
  {
    return getMessages().have(Messages.Severity.WARNING, key);
  }

  public final boolean haveError(final Object key)
  {
    return getMessages().have(Messages.Severity.ERROR, key);
  }

  public final List<String> getInfo(final Object key)
  {
    return getMessages().get(Messages.Severity.INFO, key);
  }

  public final List<String> getWarning(final Object key)
  {
    return getMessages().get(Messages.Severity.WARNING, key);
  }

  public final List<String> getError(final Object key)
  {
    return getMessages().get(Messages.Severity.ERROR, key);
  }

  public final boolean isSeverestMessageError()
  {
    return getMessages().getMaxSeverity() == Messages.Severity.ERROR;
  }

  public final boolean isSeverestMessageWarning()
  {
    return getMessages().getMaxSeverity() == Messages.Severity.WARNING;
  }

  public final boolean isSeverestMessageInfo()
  {
    return getMessages().getMaxSeverity() == Messages.Severity.INFO;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
