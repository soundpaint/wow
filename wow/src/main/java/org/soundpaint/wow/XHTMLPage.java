package org.soundpaint.wow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

public abstract class XHTMLPage extends Resource
{
  private static final Logger logger = LogManager.getLogger(XHTMLPage.class);
  protected static final String XSD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private int activeLevel1Index;
  private int activeLevel2Index;
  private int activeLevel3Index;

  private void initMenuIndices()
  {
    activeLevel1Index = -1;
    activeLevel2Index = -1;
    activeLevel3Index = -1;
    final ResourceContext context = getContext();
    final String requestURIPath = context.getRequest().getRequestURI();
    final int level1Count = getLevel1Count();
    // TODO: Generalize the following code for arbitrary amount of levels.
    outer_loop: for (int level1Index = 0; level1Index < level1Count; level1Index++) {
      int level2Count = getLevel2Count(level1Index);
      for (int level2Index = 0; level2Index < level2Count; level2Index++) {
        final int level3Count =
            context.getLevel3Count(level1Index, level2Index);
        for (int level3Index = 0; level3Index < level3Count; level3Index++) {
          final String entryPath =
              context.getLevel3Path(level1Index, level2Index, level3Index);
          if (requestURIPath.equals(entryPath)) {
            activeLevel1Index = level1Index;
            activeLevel2Index = level2Index;
            activeLevel3Index = level3Index;
            break outer_loop;
          }
        }
      }
    }
    /*
     * if (activeGroupIndex == -1) // assertion failure throw new
     * IllegalStateException("no matching navigator group for " +
     * "request URI path " + requestURIPath); if (activeEntryIndex == -1) //
     * assertion failure throw new
     * IllegalStateException("no matching navigator entry for " +
     * "request URI path " + requestURIPath); if (activeTabIndex == -1) //
     * assertion failure throw new
     * IllegalStateException("no matching tab index for " + "request URI path "
     * + requestURIPath);
     */
  }

  final public void init(final ResourceContext resourceContext)
    throws IOException
  {
    super.init(resourceContext);
    initMenuIndices();
  }

  @Override
  protected ResponseBody createResponseBodyForResponse(
    final HttpServletResponse response) throws IOException, ServletException
  {
    final String xhtml = generateXHTML();
    final ResponseBody responseBody = new ResponseBody() {
      private final PrintWriter out;
      {
        out = XHTMLPage.this.getContext().getWriter();
      }

      public void write() throws IOException {
        out.write(xhtml);
      }

      public void flush() throws IOException {
        out.flush();
      }
    };
    response.setContentType("application/xhtml+xml; charset=UTF-8");
    return responseBody;
  }

  @Override
  protected final ResponseBody serveActions(final ResourceContext context)
    throws IOException, ServletException
  {
    context.setAllowRedirect(true);
    logger.debug("serving actions"); // DEBUG
    serveActions();
    context.setAllowRedirect(false);
    if (context.isRedirected()) {
      logger.debug("redirect => abort handling"); // DEBUG
      return new ResponseBody() {
        public void write() {}
        public void flush() {}
      };
    }
    return null;
  }

  /**
   * This method is the second of all page-specific methods that gets called.
   * Override this method to implement action handling. If you want to apply a
   * page redirection, then put a call to the <code>redirect</code> method into
   * the implementation of this method (e.g.
   * <code>redirect("/path-of-new-URL");</code>) and ensure that the
   * implementation immediately afterwards returns to the caller of this method
   * (e.g. by putting a <code>return</code> statement immediately after the
   * invocation statement of method <code>redirect()</code>). If you can not
   * immediately return, make sure that you do not execute any code that relies
   * on the page being further processed. For this purpose, you may want to call
   * method <code>isRedirected</code> in order to check if you previously called
   * method <code>redirect()</code>. This check is in particular useful if you
   * have a <code>finally</code> block in your implementation of this method
   * that will be called in either case.
   * 
   * The default implementation of this method does nothing.
   */
  protected void serveActions() throws ServletException, IOException
  {
    // default implementation does nothing
  }

  protected abstract String generateXHTML()
    throws ServletException, IOException;

  public static final String buildAttribute(final String name,
                                            final String value)
  {
    return value != null
      ? " " + name + "=\"" + attributeQuote(value) + "\""
      : "";
  }

  /**
   * Like cDataQuote, but also quotes double quote characters.
   */
  private static final String attributeQuote(final String text)
  {
    // For performance reasons, we do *not* follow the straight-forward approach
    // of calling cDataQuote() as first step and then modify the result.
    // Instead, we duplicate code from cDataQuote() and mix it with the
    // additionally needed code.
    if (text == null)
      return null;
    final StringBuffer quoted = new StringBuffer();
    for (int i = 0; i < text.length(); i++) {
      final char ch = text.charAt(i);
      if (ch == '<') {
        quoted.append("&lt;");
      } else if (ch == '>') {
        quoted.append("&gt;");
      } else if (ch == '&') {
        quoted.append("&amp;");
      } else if (ch == '"') {
        quoted.append("&quot;");
      } else {
        quoted.append(ch);
      }
    }
    return quoted.toString();
  }

  public static final String cDataQuote(final String text)
  {
    if (text == null) { return null; }
    final StringBuffer quoted = new StringBuffer();
    for (int i = 0; i < text.length(); i++) {
      final char ch = text.charAt(i);
      if (ch == '<') {
        quoted.append("&lt;");
      } else if (ch == '>') {
        quoted.append("&gt;");
      } else if (ch == '&') {
        quoted.append("&amp;");
      } else {
        quoted.append(ch);
      }
    }
    return quoted.toString();
  }

  /**
   * TODO: This method should take a Resource class and HTTP URL parameters as
   * parameters rather than a String that contains the location URL. Maybe, each
   * compiled resource should have a "redirectToMe" method that requires all
   * necessary HTTP URL parameters as arguments and automatically creates the
   * redirect location.
   * 
   * @deprecated Use a redirect method with a Resource class as argument.
   */
  @Deprecated
  protected final void redirect(final String location) throws ServletException
  {
    final ResourceContext context = getContext();
    if (!context.isAllowRedirect()) {
      throw new ServletException("may redirect only while serving actions");
    }
    try {
      context.getResponse().sendRedirect(location);
      context.setRedirected(true);
    } catch (final IOException e) {
      throw new ServletException("redirect failed", e);
    }
  }

  /*
   * TODO: Add argument for passing URL parameters.
   * 
   * @param resource
   * @throws ServletException
   */
  /*
  protected final void redirect(PageClassesLoader.ResourceControl resource)
      throws ServletException {
    final ResourceContext context = getContext();
    if (!context.allowRedirect) { throw new ServletException(
        "may redirect only while serving actions"); }
    final String navigationPath = resource.getNavigationPath();
    final URI redirectURI = getURI(navigationPath);
    try {
      context.getResponse().sendRedirect(redirectURI.toString());
      context.redirected = true;
    } catch (final IOException e) {
      throw new ServletException("redirect failed", e);
    }
  }
  */

  final boolean isRedirected()
  {
    return getContext().isRedirected();
  }

  private static final int ID_COUNTER_RUNTIME_INCREMENT = 1 << 32;

  private final HashMap<Integer, Long> dynamicIdCounters =
    new HashMap<Integer, Long>();

  protected final String getId(final int compileTimePortion)
  {
    final Integer objCompileTimePortion = compileTimePortion;
    final long id;
    if (!dynamicIdCounters.containsKey(objCompileTimePortion)) {
      id = 0;
    } else {
      id =
        dynamicIdCounters.get(objCompileTimePortion) +
        ID_COUNTER_RUNTIME_INCREMENT;
    }
    dynamicIdCounters.put(objCompileTimePortion, id);
    return Long.toString(id + compileTimePortion);
  }

  protected static final String getLogoffTitle(final ResourceContext context)
  {
    return
      context.getI18N().get_title_logoff(
      context.getSession().getLoginName().getValue());
  }

  protected final int getIssuesCount()
  {
    return getContext().getIssuesCount();
  }

  protected final String getIssueTitle(final int index)
  {
    return getContext().getIssueTitle(index);
  }

  protected final String getIssueTextStart(final int index)
  {
    return getContext().getIssueTextStart(index);
  }

  protected final int getLevel1Count()
  {
    return getContext().getLevel1Count();
  }

  protected static final String getLevel1Label(final ResourceContext context,
                                               final int level1Index)
  {
    return context.getLevel1Label(level1Index, context.getI18N());
  }

  protected final int getLevel2Count(final int level1Index)
  {
    return getContext().getLevel2Count(level1Index);
  }

  protected final String getLevel2Id(final int level1Index,
                                     final int level2Index)
  {
    return
      (level1Index == activeLevel1Index) && (level2Index == activeLevel2Index)
      ? "active"
      : null;
  }

  protected static final String getLevel2OrdinalNumber(final int level1Index,
                                                       final int level2Index)
  {
    return level2Index + ".";
  }

  protected final String getLevel2Label(final ResourceContext context,
                                        final int level1Index,
                                        final int level2Index)
  {
    return context.getLevel2Label(level1Index, level2Index, context.getI18N());
  }

  protected final String getLevel2URI(final int level1Index,
                                      final int level2Index)
  {
    return getContext().getLevel2URI(level1Index, level2Index).toString();
  }

  protected final boolean isLevel3Page()
  {
    return (activeLevel1Index != -1) && (activeLevel2Index != -1);
  }

  protected final int getLevel3Count()
  {
    return (activeLevel1Index != -1) && (activeLevel2Index != -1)
      ? getContext().getLevel3Count(activeLevel1Index, activeLevel2Index)
      : 1;
  }

  protected final String getLevel3Id(final int level3Index)
  {
    return
      (activeLevel3Index == -1) || (level3Index == activeLevel3Index)
      ? "current"
      : "internal-link-pos-" + level3Index;
  }

  protected final String getLevel3Class(final int level3Index)
  {
    return
      (activeLevel3Index == -1) || (level3Index == activeLevel3Index)
      ? "activelink"
      : null;
  }

  protected final String getLevel3HRef(final int level3Index)
  {
    if ((activeLevel1Index != -1) && (activeLevel2Index != -1)) {
      return
        getContext().
        getLevel3HRef(activeLevel1Index, activeLevel2Index, level3Index);
    } else {
      // assertion
      throw new IllegalStateException("this page is not a level #3 page");
    }
  }

  protected final String getLevel3Name(final int level3Index)
  {
    if ((activeLevel1Index != -1) && (activeLevel2Index != -1)) {
      return
        getContext().
        getLevel3Name(activeLevel1Index, activeLevel2Index,
                      level3Index, getContext().getI18N());
    } else {
      // assertion
      throw new IllegalStateException("this page is not a level #3 page");
    }
  }

  protected final boolean isLevel3Active(final int level3Index)
  {
    return
      (activeLevel3Index == -1) ||
      (activeLevel3Index == level3Index);
  }

  protected static final String getEvenOdd(final int index)
  {
    return (index & 0x1) == 0 ? "even" : "odd";
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
