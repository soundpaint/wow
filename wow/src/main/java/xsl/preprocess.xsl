<!DOCTYPE stylesheet [
  <!ENTITY nl "&#x0a;">
]>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:html="http://www.w3.org/1999/xhtml"
  xmlns:wow="https://www.soundpaint.org/wow">

  <xsl:output method="xml" indent="yes"/>
  <!-- <xsl:preserve-space elements="*"/> -->
  <!-- <xsl:strip-space elements="*"/> -->

  <xsl:param name="template-dir-path"/>
  <xsl:param name="template-base-name"/>
  <xsl:param name="compile-datetime"/>

  <xsl:template match="/">
    <xsl:text>&nl;</xsl:text>
    <html>
      <xsl:copy-of select="@*"/>
      <xsl:call-template name="template-parameters"/>
      <xsl:call-template name="html-head"/>
      <xsl:call-template name="html-body"/>
    </html>
    <xsl:text>&nl;</xsl:text>
  </xsl:template>

  <xsl:template name="template-parameters">
    <xsl:text>&nl;  </xsl:text>
    <intermediate-node name='template-dir-path'>
      <xsl:value-of select="$template-dir-path"/>
    </intermediate-node>
    <xsl:text>&nl;  </xsl:text>
    <intermediate-node name='template-base-name'>
      <xsl:value-of select="$template-base-name"/>
    </intermediate-node>
    <xsl:text>&nl;  </xsl:text>
    <intermediate-node name='compile-datetime'>
      <xsl:value-of select="$compile-datetime"/>
    </intermediate-node>
  </xsl:template>

  <xsl:template name="html-head">
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <meta name="description">
        <xsl:attribute name="content"
                       select="/wow:template/wow:meta/wow:description" />
      </meta>
      <meta name="keywords">
        <xsl:attribute name="content"
                       select="/wow:template/wow:meta/wow:keywords" />
      </meta>
      <link rel="stylesheet" href="../css/base.css" type="text/css"/>
      <link rel="icon" href="../images/wow.png"/>
      <title>
        <xsl:value-of select="/wow:template/wow:meta/wow:title"/>
      </title>
    </head>
  </xsl:template>

  <xsl:template name="html-body">
    <body>
      <div id="frame">
        <!-- TODO: Bread Crumb Navigation -->
        <xsl:call-template name="left-sidebar"/>
        <div id="main">
          <h2>
            <xsl:value-of select="/wow:template/wow:meta/wow:title"/>
          </h2>
          <div id="content">
            <form method="post" enctype="multipart/form-data">
              <xsl:for-each select="/wow:template/wow:content">
                <xsl:apply-templates/>
              </xsl:for-each>
            </form>
          </div>
          <xsl:call-template name="footer"/>
        </div>
      </div>
    </body>
  </xsl:template>

  <xsl:template name="branding-area">
    <div class="branding-area">
      <img style="border:0;width:92px;height:32px"
           src="../images/wow_green.png" alt="wow logo"/>
    </div>
    <div class="branding-area" style="padding-top:10px">
      <span style="padding-left:115px">Wow…</span><br/>
      <span style="padding-left:130px">Web</span>
    </div>
  </xsl:template>

  <xsl:template name="page-navigation">
    <nav class="page-nav">
      <div class="active-nav">
        <span>Welcome to Wow…</span>
      </div>
      <div class="inactive-nav">
        <a href="Login">Login</a>
      </div>
    </nav>
  </xsl:template>

  <xsl:template name="sidebar-footer">
    <div id="sidebar-footer">
      <nav class="lang-nav">
        <p>
          <span class="inactive-lang">
            <xsl:text>🇬🇧 English</xsl:text>
          </span>
          <br/>
          <span class="active-lang">
            <a href="?lang=de">🇩🇪 Deutsch</a>
          </span>
        </p>
      </nav>
      <p>
        <a href="https://validator.w3.org/check?uri=referer">
          <xsl:text>Nu HTML Check</xsl:text>
        </a>
        <br />
        <a href="https://jigsaw.w3.org/css-validator/check/referer">
          <img style="border:0;width:88px;height:31px"
               src="https://jigsaw.w3.org/css-validator/images/vcss-blue"
               alt="Valid CSS!"/>
        </a>
      </p>
      <p>
        <xsl:text>Page last compiled:</xsl:text><br/>
        <xsl:value-of select="$compile-datetime"/>
      </p>
    </div>
  </xsl:template>

  <!--  example of matching a wow tag -->
  <xsl:template name="left-sidebar">
    <div class="left-sidebar">
      <xsl:call-template name="branding-area"/>
      <xsl:call-template name="page-navigation"/>
      <xsl:call-template name="sidebar-footer"/>
    </div>
  </xsl:template>

  <xsl:template name="footer">
    <div id="footer">
      <hr style="clear: both"/>
      <div id="address">
        <address>
          <xsl:text>This website is maintained by</xsl:text>
          <a href="https://www.juergen-reuter.de/">Jürgen Reuter</a>
          <xsl:text>.</xsl:text>
        </address>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="wow:i18n">
    <span><wow:expand key="{@key}"/></span>
  </xsl:template>

  <xsl:template match="node()">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
