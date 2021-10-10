package org.soundpaint.wow;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.log.RuntimeLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/*")
@MultipartConfig
public class RequestDispatcher extends HttpServlet
{
  static
  {
    // ensure runtime configuration is initialized before doing anything else
    RuntimeConfig.init();
  }

  private static final long serialVersionUID = -3466153890443756843L;
  private static final Logger logger =
    LogManager.getLogger(RequestDispatcher.class);

  // TODO: Ensure all compiled templates are loaded.
  /*
  static {
    try {
      PageClassesLoader.registerAllResources();
    } catch (final ServletException e) {
      throw new RuntimeException("failed registering resource classes", e);
    }
  }
  */

  private static String getBasePath()
  {
    final String relPath = "../../..";
    final URL pathURL = DefaultPage.class.getResource(relPath);
    if (pathURL != null) {
      return pathURL.getPath();
    }
    return null;
  }

  private static String transform(final String basePath,
                                  final String xmlBaseName,
                                  final String dirPath,
                                  final String sourceSuffix,
                                  final String targetSuffix,
                                  final Transformer transformer)
      throws TransformerException
  {
    final String sourceName = xmlBaseName + sourceSuffix;
    final String targetName = xmlBaseName + targetSuffix;
    final String sourceFilePath = basePath + dirPath + "/" + sourceName;
    final Source source = new StreamSource(sourceFilePath);
    final String targetFilePath = basePath + "/" + targetName;
    final Result target = new StreamResult(targetFilePath);
    transformer.transform(source, target);
    return targetFilePath;
  }

  private static final String XSD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final DateTimeFormatter xsdDateTimeFormatter =
    DateTimeFormatter.ofPattern(XSD_DATETIME_FORMAT);
  
  private static String compileTemplate(final String basePath,
                                        final String templateDirPath,
                                        final String templateBaseName)
    throws TransformerException
  {
    final LocalDateTime now = LocalDateTime.now();

    // TODO: This will not work within .jar files.
    if (!new File(basePath + "templates/" + templateBaseName + ".xml").exists()) {
      return null;
    }

    //final TransformerFactory factory = TransformerFactory.newInstance(); // TODO
    final TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    final Source preprocess = new StreamSource(basePath + "/xsl/preprocess.xsl");
    final Transformer preprocessor = factory.newTransformer(preprocess);
    final Source codeGenerate = new StreamSource(basePath + "/xsl/code-generate.xsl");
    final Transformer codeGenerator = factory.newTransformer(codeGenerate);
    preprocessor.setParameter("template-dir-path", templateDirPath);
    preprocessor.setParameter("template-base-name", templateBaseName);
    final String xsdDateTimeNow = xsdDateTimeFormatter.format(now);
    preprocessor.setParameter("compile-datetime", xsdDateTimeNow);
    transform(basePath, templateBaseName, "/templates", ".xml", ".cdom", preprocessor);
    return transform(basePath, templateBaseName, "", ".cdom", ".java", codeGenerator);
  }

  private static Class<? extends Resource> getResourceClassForTemplate(
    final String templateDirPath,
    final String templateBaseName)
  {
    final String basePath = getBasePath();
    logger.debug("basePath=" + basePath);
    logger.debug("templateDirPath=" + templateDirPath);
    logger.debug("templateBaseName=" + templateBaseName);
    String javaFilePath;
    try {
      javaFilePath = compileTemplate(basePath, templateDirPath, templateBaseName);
    } catch (final TransformerException e) {
      javaFilePath = null;
    }
    logger.debug("javaFilePath=" + javaFilePath);
    if (javaFilePath == null) return null;
    logger.info("compiling " + javaFilePath);

    final String classpath =
      basePath + File.pathSeparator +
      "/home/reuter/.p2/pool/plugins/jakarta.servlet_5.0.0.v20210105-0527.jar" +
      File.pathSeparator +
      System.getProperty("java.class.path");
    logger.debug("classpath=" + classpath);
    final List<String> optionList = new ArrayList<String>();
    optionList.addAll(Arrays.asList("-classpath", classpath));
    final Path sourcePath = Path.of(javaFilePath);
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final StandardJavaFileManager fileManager =
      compiler.getStandardFileManager(null, null, null);
    final File sourceFile = sourcePath.toFile();
    logger.debug("sourceFile=" + sourceFile);
    final Iterable<? extends JavaFileObject> compilationUnits =
      fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
    final CompilationTask compileTask =
      compiler.getTask(null, fileManager, null, optionList,
                       null, compilationUnits);
    final boolean success = compileTask.call();
    try {
      fileManager.close();
    } catch (final IOException e) {
      logger.error("failed closing file manager: " + e);
      return null;
    }
    if (!success) {
      logger.error("template compiler returned with error " + success);
      return null;
    }
    final String templateClazzName = templateBaseName + ".class";
    final Path clazzPath = sourcePath.getParent().resolve(templateClazzName);
    logger.debug("clazzPath=" + clazzPath);

    try {
      final URL classURL = clazzPath.getParent().toFile().toURI().toURL();
      logger.info("classUrl=" + classURL);
      final URLClassLoader classLoader =
        new URLClassLoader(new URL[] { classURL },
                           RequestDispatcher.class.getClassLoader());
      logger.info("classLoader=" + classLoader);
      final Class<?> clazz = Class.forName(templateBaseName, true, classLoader);
      logger.info("clazz=" + clazz);
      if (!Resource.class.isAssignableFrom(clazz)) {
        return null;
      }
      logger.info("clazz is assignable");
      return (Class<? extends Resource>)clazz;
    } catch (final MalformedURLException | ClassNotFoundException e) {
      // class not found or not ready for execution
      logger.error("exception: " + e);
      return null;
    }
  }

  private static boolean
    tryServeExecutableResource(final ResourceContext resourceContext)
    throws ServletException, IOException
  {
    final HttpServletRequest request = resourceContext.getRequest();
    /*
     * TODO: Recompile only if compiled class does not exist or its time-stamp is
     * older than that of the template.
     */
    logger.info("trying to serve executable resource");
    final String templateDirPath = ""; // TODO
      //stripOffLeadingSlash(request.getContextPath());
    logger.debug("templateDirPath=" + templateDirPath);
    final String templateBaseName =
      stripOffLeadingSlash(request.getPathInfo());
    logger.debug("templateBaseName=" + templateBaseName);

    String renderedPage;
    final HttpServletResponse response = resourceContext.getResponse();
    try {
      final Class<? extends Resource> clazz =
        getResourceClassForTemplate(templateDirPath, templateBaseName);
      if (clazz == null) {
        // no such executable resource to serve
        logger.error("no template found for execution: " +
                    "path=" + templateDirPath + ", base=" + templateBaseName);
        return false;
      }
      logger.debug("serving page from class " + clazz);
      final Method renderPageMethod =
        clazz.getMethod("renderPage", ResourceContext.class);
      renderedPage = (String)renderPageMethod.invoke(null, resourceContext);
    } catch (IllegalAccessException | IllegalArgumentException |
             InvocationTargetException | NoSuchMethodException |
             SecurityException e)
    {
      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      final StringWriter sw = new StringWriter();
      final PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      pw.flush();
      logger.error("serving page failed: " + sw);
      return true;
    }
    final String content = renderedPage;
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Connection", "close");

    /*
     * TODO: Can we compute the UTF-8 length of the large string more
     * efficiently rather than copying all data into a byte array?
     */
    final byte[] utf8Bytes = content.getBytes("UTF-8");
    response.setContentLength(utf8Bytes.length);

    final PrintWriter out = resourceContext.getWriter();
    out.print(content);
    out.flush();
    return true;
  }

  private static String stripOffLeadingSlash(final String s)
  {
    final String result;
    if ((s != null) && s.startsWith("/")) {
      result = s.substring(1);
    } else {
      logger.warn("missing leading slash in request sub-path: " + s);
      result = s;
    }
    return result;
  }

  private static I18N getFailSaveI18N(final ResourceContext resourceContext)
  {
    return resourceContext.getSession().getI18N();
  }

  private static Resource createResource(final ResourceContext resourceContext)
  {
    // Get full Resource class name.
    final HttpServletRequest request = resourceContext.getRequest();
    final String pathInfo = request.getPathInfo();
    final String relativePathInfo = stripOffLeadingSlash(pathInfo);
    logger.debug("serving " + relativePathInfo);

    // Get resource handler class.
    final ResourcesRegistry registry = ResourcesRegistry.getDefaultInstance();
    final Class<? extends Resource> clazz;
    try {
      clazz = registry.lookup(relativePathInfo);
    } catch (final java.lang.InternalError e) {
      final String message =
        "request dispatcher: " +
        "no handler found for dispatching resource \"" + relativePathInfo +
        "\"; available resources: " + registry.listKeys();
      logger.error(message);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    }

    // Get Resource class object instance.
    try {
      logger.debug("lookup succeeded: " +
                   ResourcesRegistry.getDefaultInstance().listKeys());
      logger.debug("creating instance for class " + clazz.getName());
      final Constructor<? extends Resource> constructor =
        clazz.getConstructor();
      return constructor.newInstance();
    } catch (final NoSuchMethodException e) {
      // should never occur since every class has a default
      // constructor
      final String message =
        "failed instantiating " + clazz + ": " +
        "no such constructor: " + e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    } catch (final SecurityException e) {
      // occurs e.g. when requesting the resource for a non-existing navigation
      // path
      final String message =
        "failed instantiating " + clazz + ": " +
        "default constructor not accessable for " + relativePathInfo + ": " +
        e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    } catch (final InvocationTargetException e) {
      // resource-specific error
      final String message =
        "failed instantiating " + clazz + ": " +
        "invocation target: " + e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    } catch (final IllegalAccessException e) {
      // should never occur
      final String message =
        "failed instantiating " + clazz + ": " +
        "illegal access: " + e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    } catch (final InstantiationException e) {
      // should never occur
      final String message =
        "failed instantiating " + clazz + ": " +
        "instantiation: " + e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    } catch (final ExceptionInInitializerError e) {
      // fatal error in user-provided code
      final String message =
        "failed instantiating " + clazz + ": " +
        "exception in initializer: " + e.getMessage();
      logger.error(message, e);
      throw new InternalException(message, getFailSaveI18N(resourceContext), e);
    }
  }

  private void doUnguardedRequest(final HttpServletRequest request,
                                  final HttpServletResponse response)
  {
    // Create ResourceContext object instance.
    final ResourceContext resourceContext;
    try {
      resourceContext = new ResourceContext(request, response);
    } catch (final ServletException e) {
      // Without valid resource context, there is no point in creating
      // output. Hence, just log an error and quit.
      logger.error(e.toString(), e);
      return;
    }

    ResponseBody responseBody;
    // Collect output in ResponseBody object.
    try {
      if (tryServeExecutableResource(resourceContext)) {
        return; // page served
      }

      // Initialize resource.
      Resource resource;
      try {
        resource = createResource(resourceContext);
      } catch (final InternalException e) {
        logger.error(e.toString(), e);
        final URI redirectURI = new URI("/InternalError"); // TODO
        /*
            InternalError.resolveAgainstWebRoot(buildNavigationPathURI(
                InternalError.DEFAULT_NAVIGATION_PATH, e));
                */
        logger.debug("redirecting to " + redirectURI);
        response.sendRedirect(redirectURI.toString());
        resource = null;
      }

      if (resource != null) {
        try {
          resource.init(resourceContext);
          responseBody = resource.serve();
        } catch (final SecurityException e) {
          logger.error(e.toString(), e);
          final URI redirectURI = new URI("/AccessError"); // TODO
          /*
              AccessError.resolveAgainstWebRoot(buildNavigationPathURI(
                  AccessError.DEFAULT_NAVIGATION_PATH, e));
                  */
          logger.debug("redirecting to " + redirectURI);
          response.sendRedirect(redirectURI.toString());
          responseBody = null;
        } catch (final InvalidURLException e) {
          logger.error(e.toString(), e);
          final URI redirectURI = new URI("/InvalidURLError"); // TODO
          /*
              InvalidURLError.resolveAgainstWebRoot(buildNavigationPathURI(
                  InvalidURLError.DEFAULT_NAVIGATION_PATH, e));
                  */
          logger.debug("redirecting to " + redirectURI);
          response.sendRedirect(redirectURI.toString());
          responseBody = null;
        }
      } else {
        responseBody = null;
      }
    } catch (final Throwable t) {
      logger.error(t.toString(), t);
      try {
        final URI redirectURI = new URI("/InternalError"); // TODO
        /*
            InternalError.resolveAgainstWebRoot(buildNavigationPathURI(
                InternalError.DEFAULT_NAVIGATION_PATH, t));
                */
        logger.debug("redirecting to " + redirectURI);
        response.sendRedirect(redirectURI.toString());
      } catch (final IOException | URISyntaxException e) {
        // can not even redirect => just log and then stop trying anything else
        logger.error(e.toString(), e);
      }
      responseBody = null;
    }

    if (responseBody != null) {
      /*
       * Finally succeeded to generate a valid response body (possibly
       * an error message). Submit it to the client.
       */
      try {
        responseBody.write();
      } catch (final IOException e) {
        /*
         * Since submission to the client failed, there is no point in
         * trying to send yet another message (about the error) to the
         * client. Instead, just log the incident and quit.
         */
        logger.error(e.toString(), e);
        return;
      } finally {
        /*
         * Do *not* close the output stream in case of using keep-alive
         * connections. Instead, just flush the stream and response
         * object.
         */
        try {
          responseBody.flush();
        } catch (final Exception ex) {
          logger.error("failed flushing response body: " + ex);
        }
        try {
          response.flushBuffer();
        } catch (final Exception ex) {
          logger.error("failed flushing response buffer: " + ex);
        }
      }
    } else {
      /*
       * (responseBody == null), i.e. a redirect has been sent.
       * Nothing more to do here.
       */
    }
  }

  private void doRequest(final HttpServletRequest request,
                         final HttpServletResponse response)
  {
    boolean showDetailedInternalErrors = true;
    try {
      showDetailedInternalErrors =
        RuntimeConfig.getDefaultInstance().
        getOptionsConfig().showDetailedInternalErrors;
      doUnguardedRequest(request, response);
    } catch (final Throwable lastResortThrowable) {
      // Still caught an error despite elaborated exception handling.
      // Hence, give finally up to post anything further as response.
      // But at least do log this incident before returning to the
      // caller.
      logger.error("failed serving resource " + request.getPathInfo() + ": " +
                   lastResortThrowable);
      if (showDetailedInternalErrors) {
        throw lastResortThrowable; // throw full error
      } else {
        // Drop origin exception, only keep message.
        throw new RuntimeException(lastResortThrowable.getMessage());
      }
    }
  }

  public void doGet(final HttpServletRequest request,
                    final HttpServletResponse response)
    throws ServletException, IOException
  {
    logger.info("do get()");
    doRequest(request, response);
  }

  public void doPost(final HttpServletRequest request,
                     final HttpServletResponse response)
  throws ServletException, IOException
  {
    doGet(request, response);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
