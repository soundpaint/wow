package org.soundpaint.wow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.soundpaint.wow.db.types.DBString;
import org.soundpaint.wow.utils.ParseException;

import jakarta.servlet.ServletException;

/**
 * Servlet implementation class DefaultPage
 *
 * This temporary class is a manually "compiled" template
 * that helps to implement the template compiler and
 * check the compiler's output against this sample
 * implementation of a compiled template.
 *
 * This class will be thrown away once the template
 * compiler is sufficiently mature.
 */
public class DefaultPage extends XHTMLPage {

  private static final String DEFAULT_NAVIGATION_PATH = "DefaultPage";

  public String getDefaultNavigationPath() {
    return "/" + DEFAULT_NAVIGATION_PATH;
  }

  private static final Map<String, ParameterParser> parameterParsers;

  static {
    final Class<? extends Resource> clazz = DefaultPage.class;
    ResourcesRegistry.getDefaultInstance().register(DEFAULT_NAVIGATION_PATH, clazz);
    parameterParsers = new HashMap<String, ParameterParser>();
    parameterParsers.put(
      "lang",
      new SingleParameterParser() {
        protected void parseValue(final Resource resource, final String parameterValue) throws ParseException {
          final DefaultPage outerClass = (DefaultPage)resource;
          assert outerClass._lang == null : "duplicate parameter key";
          outerClass._lang =
            DBString.parseLocalized(resource.getContext().getI18N(),
                                    parameterValue);
        }
      }
    );
    parameterParsers.put(
        "encodingTest",
        new SingleParameterParser() {
          protected void parseValue(final Resource resource, final String parameterValue) throws ParseException {
            final DefaultPage outerClass = (DefaultPage)resource;
            assert outerClass._encodingTest == null : "duplicate parameter key";
            outerClass._encodingTest =
              DBString.parseLocalized(outerClass.getContext().getI18N(),
                                      parameterValue);
          }
        }
      );
    }

  // HTML form URL parameters of this page
  protected DBString _lang;
  protected DBString _encodingTest;

  /**
   * Default constructor. 
   */
  public DefaultPage(final ResourceContext resourceContext) {
    super(resourceContext);
  }

  /*
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  /*
  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
    throws ServletException, IOException
  {
    for (int paramCount = 0; paramCount < TODO; paramCount++) {
      final String fieldName = "whatever"; // TODO
      final boolean isFormField = false; // TODO
      final boolean isMultiple = false; // TODO
      parseParameters(request, fieldName, isFormField, isMultiple);
    }
    // TODO: Actually perform post.
    doGet(request, response);
  }
  */

  @Override
  public void parseParameters()
    throws ParseException, ServletException, IOException
  {
    parseParameters(parameterParsers);
  }

  /*
  private void parseParameter(final Map<String, ParameterParser> parameterParsers,
                              final String fileName,
                              final String[] values)
  {
  // TODO
  }
  */

  @Override
  public String generateXHTML()
  {
    return "blubb";
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
