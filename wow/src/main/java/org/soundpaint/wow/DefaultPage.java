package org.soundpaint.wow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.soundpaint.wow.db.types.DBString;
import org.soundpaint.wow.utils.ParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import jakarta.servlet.ServletException;

/**
 * Servlet implementation class DefaultPage
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
  public DefaultPage() {
    // TODO Auto-generated constructor stub
  }

  public static String renderPage(final ResourceContext resourceContext)
  {
    return "blah"; // TODO
  }

  private static String getBasePath()
  {
    final String relPath = "../../..";
    final URL pathURL = DefaultPage.class.getResource(relPath);
    if (pathURL != null) {
      return pathURL.getPath();
    }
    return null;
  }

  private static String transform(final String xmlBaseName,
                                 final String dirPath,
                                 final String sourceSuffix,
                                 final String targetSuffix,
                                 final Transformer transformer)
      throws TransformerException
  {
    final String sourceName = xmlBaseName + sourceSuffix;
    final String targetName = xmlBaseName + targetSuffix;
    final String basePath = getBasePath();
    final String sourceFilePath = basePath + dirPath + "/" + sourceName;
    final Source source = new StreamSource(sourceFilePath);
    final String targetFilePath = basePath + "/" + targetName;
    final Result target = new StreamResult(targetFilePath);
    transformer.transform(source, target);
    return targetFilePath;
  }

  private static String compileTemplate(final String templateDirPath,
                                        final String templateBaseName)
    throws TransformerException
  {
    //final TransformerFactory factory = TransformerFactory.newInstance();
    final TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
    //final TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    final String basePath = getBasePath();
    final Source preprocess = new StreamSource(basePath + "/xsl/preprocess.xsl");
    final Transformer preprocessor = factory.newTransformer(preprocess);
    final Source codeGenerate = new StreamSource(basePath + "/xsl/code-generate.xsl");
    final Transformer codeGenerator = factory.newTransformer(codeGenerate);
    preprocessor.setParameter("template-dir-path", templateDirPath);
    preprocessor.setParameter("template-base-name", templateBaseName);
    transform(templateBaseName, "/templates", ".xml", ".cdom", preprocessor);
    return transform(templateBaseName, "", ".cdom", ".java", codeGenerator);
  }

  /*
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  /*
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }
  */

  /*
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  /*
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

  public String generateXHTML()
  {
    return "blubb";
  }

  private String _generateXHTML_obsolete()
  {
    /*
    final ServletContext servletContext = getServletContext();
    servletContext.addServlet("hello", (Class<? extends Servlet>) Object.class);
    */
    final String templateDirPath = "";
    final String templateBaseName = "Index";
    String javaFilePath;

    try {
      javaFilePath = compileTemplate(templateDirPath, templateBaseName);
    } catch (final TransformerException e) {
      //out.println("[exception: " + e + "]");
      javaFilePath = null;
    }
    final Path sourcePath = Path.of(javaFilePath);

    //out.println("[request-path-info=" + request.getPathInfo() + "]");
    //final String sourceCode = createSourceCode();
    //String tmpDir = System.getProperty("java.io.tmpdir");
    //final Path sourcePath = Paths.get(tmpDir, "Test1.java");
    //out.println("[saving code as " + sourcePath + "]");
    //Files.write(sourcePath, sourceCode.getBytes("UTF-8"));
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    compiler.run(null, null, null, sourcePath.toFile().getAbsolutePath());
    //final Path clazzPath = sourcePath.getParent().resolve("Test1.class");
    final String templateClazzName = templateBaseName + ".class";
    final Path clazzPath = sourcePath.getParent().resolve(templateClazzName);
    //out.println("[compiled code as " + clazzPath + "]");
    try {
      URL classUrl = clazzPath.getParent().toFile().toURI().toURL();
      URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
      final Class<?> clazz = Class.forName(templateBaseName, true, classLoader);
      final Object code = clazz.getDeclaredConstructor().newInstance();
      final String content = code.toString();
      return content;
      /*
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("text/html; charset=UTF-8");
      response.setHeader("Connection", "close");

      // TODO: Can we compute the UTF-8 length of the string more
      // efficiently rather than copying all data into a byte array?
      final byte[] utf8Bytes = content.getBytes("UTF-8");
      response.setContentLength(utf8Bytes.length);

      final PrintWriter out = response.getWriter();
      out.print(content);
      */
    } catch (ClassNotFoundException | InstantiationException |
             IllegalAccessException | IllegalArgumentException |
             InvocationTargetException | NoSuchMethodException |
             SecurityException | MalformedURLException e) {
      /*
      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      final PrintWriter out = response.getWriter();
      */
      //e.printStackTrace(out);
      // TODO
      return null;
    }
    //out.println();
    //out.append("[Served at: ").append(request.getContextPath()).append("]");
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
