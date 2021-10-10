<!DOCTYPE stylesheet [
  <!ENTITY nl "&#x0a;">
]>
<!--
WORKAROUND: For auto-indenting to work while adding this file, add
a link to the style sheet DTD as follows:

     SYSTEM "https://www.w3.org/1999/11/xslt10.dtd"

to the DOCTYPE declaration.  However, this change will make the
below "xmlns:html" declaration invalid.  Hence, remove it again
before compiling and running.
-->
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:dyn="http://exslt.org/dynamic"
  xmlns:saxon="http://saxon.sf.net/"
  xmlns:html="http://www.w3.org/1999/xhtml"
  xmlns:wow="https://www.soundpaint.org/wow"
  extension-element-prefixes="dyn">

  <xsl:output method="text"/>

  <!-- useful for debugging purposes -->
  <xsl:template name="print-node-type">
    <xsl:choose>
      <xsl:when test="count(.|/) = 1">
        <xsl:text>root</xsl:text>
      </xsl:when>
      <xsl:when test="self::*">
        <xsl:text>element </xsl:text>
        <xsl:value-of select="name()"/>
      </xsl:when>
      <xsl:when test="self::text()">
        <xsl:text>text</xsl:text>
      </xsl:when>
      <xsl:when test="self::comment()">
        <xsl:text>comment</xsl:text>
      </xsl:when>
      <xsl:when test="self::processing-instruction()">
        <xsl:text>pi</xsl:text>
      </xsl:when>
      <xsl:when test="count(.|../@*)=count(../@*)">
        <xsl:text>attribute</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>unknown</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="intermediate-node">
    <!-- intermediate node with meta information,
         strip off from final output. -->
  </xsl:template>

  <xsl:template match="html">
    <xsl:variable name="template-dir-path"
                  select="intermediate-node[@name='template-dir-path']/text()"/>
    <xsl:variable name="template-base-name"
                  select="intermediate-node[@name='template-base-name']/text()"/>
    <xsl:variable name="compile-datetime"
                  select="intermediate-node[@name='compile-datetime']/text()"/>
    <xsl:variable name="package-path">
      <xsl:if test="$template-dir-path != ''">
        <xsl:value-of select="replace($template-dir-path, '/', '.', 'sm')"/>
      </xsl:if>
    </xsl:variable>
    <xsl:variable name="fully-qualified-class-name">
      <xsl:if test="$package-path != ''">
        <xsl:value-of select="$package-path"/>
        <xsl:text>.</xsl:text>
      </xsl:if>
      <xsl:value-of select="$template-base-name"/>
    </xsl:variable>
    <xsl:if test="$package-path != ''">
      <xsl:text>package </xsl:text>
      <xsl:value-of select="$package-path"/>
      <xsl:text>;&nl;&nl;</xsl:text>
    </xsl:if>
    <xsl:text>import java.io.PrintWriter;&nl;</xsl:text>
    <xsl:text>import java.io.StringWriter;&nl;</xsl:text>
    <xsl:text>import java.time.LocalDateTime;&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>import jakarta.servlet.ServletException;&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>import org.soundpaint.wow.ResourceContext;&nl;</xsl:text>
    <xsl:text>import org.soundpaint.wow.XHTMLPage;&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>/**&nl;</xsl:text>
    <xsl:text> * Compiled template for resource: </xsl:text>
    <xsl:value-of select="$fully-qualified-class-name"/>
    <xsl:text>&nl;</xsl:text>
    <xsl:text> * &nl;</xsl:text>
    <xsl:text> * WARNING: This file has been automatically create on </xsl:text>
    <xsl:value-of select="$compile-datetime"/>
    <xsl:text>.&nl;</xsl:text>
    <xsl:text> *          Do not change this file, since it will eventually be&nl;</xsl:text>
    <xsl:text> *          overwritten.&nl;</xsl:text>
    <xsl:text> */&nl;</xsl:text>
    <xsl:text>public class </xsl:text>
    <xsl:value-of select="$template-base-name"/>
    <xsl:text> extends XHTMLPage&nl;</xsl:text>
    <xsl:text>{&nl;</xsl:text>
    <xsl:text>  private static final String COMPILE_XSD_DATETIME =&nl;</xsl:text>
    <xsl:text>    "</xsl:text>
    <xsl:value-of select="$compile-datetime" />
    <xsl:text>";&nl;</xsl:text>
    <xsl:text>  private static final LocalDateTime COMPILE_DATETIME =&nl;</xsl:text>
    <xsl:text>    LocalDateTime.parse(COMPILE_XSD_DATETIME, XSD_DATETIME_FORMATTER);&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  public static LocalDateTime getCompileDateTime()&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    return COMPILE_DATETIME;&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  public &nl;</xsl:text>
    <xsl:value-of select="$template-base-name"/>
    <xsl:text>(final ResourceContext context)&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    super(context);&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  @Override&nl;</xsl:text>
    <xsl:text>  public String getDefaultNavigationPath()&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    // TODO&nl;</xsl:text>
    <xsl:text>    return "</xsl:text>
    <xsl:value-of select="$package-path"/>
    <xsl:text>/</xsl:text>
    <xsl:value-of select="$template-base-name"/>
    <xsl:text>";&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  @Override&nl;</xsl:text>
    <xsl:text>  public void parseParameters()&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    // TODO&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  public void generateXHTML(final PrintWriter out)&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    out.print("&lt;!DOCTYPE html>\n");&nl;</xsl:text>
    <!-- &lt;xsl:text>    out.print("&lt;html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">");&amp;nl;&lt;/xsl:text>-->
    <xsl:text>    out.print("&lt;html lang=\"en\">");&nl;</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>    out.print("&lt;/html>\n");&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  @Override&nl;</xsl:text>
    <xsl:text>  public String generateXHTML()&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    final StringWriter sw = new StringWriter();&nl;</xsl:text>
    <xsl:text>    final PrintWriter pw = new PrintWriter(sw);&nl;</xsl:text>
    <xsl:text>    generateXHTML(pw);&nl;</xsl:text>
    <xsl:text>    pw.flush();&nl;</xsl:text>
    <xsl:text>    return sw.toString();&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>  public String toString()&nl;</xsl:text>
    <xsl:text>  {&nl;</xsl:text>
    <xsl:text>    return "wow servlet for page </xsl:text>
    <xsl:value-of select="$template-base-name"/>
    <xsl:text>";&nl;</xsl:text>
    <xsl:text>  }&nl;</xsl:text>
    <xsl:text>}&nl;</xsl:text>
    <xsl:text>&nl;</xsl:text>
    <xsl:text>/*&nl;</xsl:text>
    <xsl:text> * Local Variables:&nl;</xsl:text>
    <xsl:text> *   coding:utf-8&nl;</xsl:text>
    <xsl:text> *   mode:Java&nl;</xsl:text>
    <xsl:text> * End:&nl;</xsl:text>
    <xsl:text> */&nl;</xsl:text>
  </xsl:template>

  <xsl:template name="quote-text-as-comment">
    <xsl:param name="text"/>
    <xsl:variable name="esc-esc"
                  select="replace($text, '\\', '\\\\', 'sm')"/>
    <xsl:variable name="esc-lf"
                  select="replace(string($esc-esc), '&#x0a;', '&#x0a;     *', 'sm')"/>
    <xsl:text>    /*</xsl:text>
    <xsl:value-of select="$esc-lf"/>
    <xsl:text>*/&nl;</xsl:text>
  </xsl:template>

  <xsl:template name="quote-text-as-java-string">
    <xsl:param name="text"/>
    <xsl:variable name="esc-esc"
                  select="replace($text, '\\', '\\\\', 'sm')"/>
    <xsl:variable name="esc-lf"
                  select="replace(string($esc-esc), '&#x0a;', '\\n', 'sm')"/>
    <xsl:variable name="esc-quote"
                  select="replace(string($esc-lf), '&#x22;', '\\&#x22;', 'sm')"/>
    <xsl:value-of select="$esc-quote"/>
  </xsl:template>

  <xsl:template match="/html//comment()">
    <!-- drop core XML comments -->
    <!--
    <xsl:text>    out.print("&lt;!-&#x2d;</xsl:text>
    <xsl:call-template name="quote-text-as-java-string">
      <xsl:with-param name="text" select="string(.)"/>
    </xsl:call-template>
    <xsl:text>-&#x2d;&gt;");&nl;</xsl:text>
    -->
  </xsl:template>

  <xsl:template match="text()">
    <xsl:variable name="after-intermediate-node"
                  select="preceding-sibling::node()[1][local-name() = 'intermediate-node']"/>
    <xsl:variable name="after-code-comment"
                  select="preceding-sibling::node()[1][local-name() = 'code-comment']"/>
    <xsl:variable name="after-xml-comment"
                  select="preceding-sibling::node()[1][. instance of comment()]"/>
    <xsl:if test="not($after-intermediate-node) and
                  not($after-code-comment) and
                  not($after-xml-comment)">
      <xsl:text>    out.print("</xsl:text>
      <xsl:call-template name="quote-text-as-java-string">
        <xsl:with-param name="text" select="string(.)"/>
      </xsl:call-template>
      <xsl:text>");&nl;</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template name="render-attribute">
    <xsl:text>    out.print(" </xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:text>=\"</xsl:text>
    <xsl:value-of select="string()"/>
    <xsl:text>\"");&nl;</xsl:text>
  </xsl:template>

  <xsl:template match="wow:code-comment">
    <xsl:variable name="comment">
      <xsl:call-template name="quote-text-as-comment">
        <xsl:with-param name="text" select="string(.)"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:value-of select="$comment" />
  </xsl:template>

  <xsl:template match="wow:html-comment">
    <xsl:text>    out.print("&lt;!--</xsl:text>
      <xsl:call-template name="quote-text-as-java-string">
        <xsl:with-param name="text" select="string(.)"/>
      </xsl:call-template>
    <xsl:text>-->");&nl;</xsl:text>
  </xsl:template>

  <xsl:template match="wow:expand">
    <xsl:text>    // TODO: i18n.expand(out, xmlEncode("</xsl:text>
      <xsl:call-template name="quote-text-as-java-string">
        <xsl:with-param name="text" select="@key"/>
      </xsl:call-template>
    <xsl:text>"));&nl;</xsl:text>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <!-- TODO: Check: xsl:copy only useful for output mode XML? -->
      <xsl:text>    out.print("&lt;</xsl:text>
      <xsl:value-of select="name()"/>
      <xsl:text>");&nl;</xsl:text>
      <xsl:if test="@*">
        <xsl:for-each select="@*">
          <xsl:call-template name="render-attribute" />
        </xsl:for-each>
      </xsl:if>
      <xsl:text>    out.print("</xsl:text>
      <xsl:choose>
        <xsl:when test="node()">
          <xsl:text>&gt;");&nl;</xsl:text>
          <xsl:apply-templates select="node()"/>
          <xsl:text>    out.print("&lt;/</xsl:text>
          <xsl:value-of select="name()"/>
          <xsl:text>&gt;");&nl;</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>/&gt;");&nl;</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
