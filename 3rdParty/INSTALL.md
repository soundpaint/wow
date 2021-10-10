Compiling
=========

Additional software required:

* apache-tomcat (tested with apache-tomcat-10.0.10)
* log4j (tested with log4j-core-2.14.1.jar)
* SAXON XSLT and XQuery Processor (tested with saxon-he-10.6.jar)

For each software (.jar file) do:

* Save the software e.g. in the eclipse project space under an existing folder, say, 3rdParty.  In the Project Explorer, click on 3rdParty and, via right mouse click -> Refresh.
* [TODO: Is this necessary?] Add classpath to external .jar files via Window -> Preferences -> Java -> Build Path -> Classpath Variable.
* In the Project Explorer, click on project wow.  Then, via Project -> Properties -> Java Build Path -> Classpath -> Add JARs, selected the saved jar file under 3rdParty.
* If catalina/tomcat stillcan not find log4j ("ClassNotFoundError"), add "${catalina.base}/../../../../3rdParty/*.jar" to property "common.loader" in file "catalina.properties".
* Change config.xml in wow/src/main/java (or wow/src/main/webapp/WEB-INF/config.xml???)  as appropriate.

Configuring
===========

Ensure that the following lines (or similar contents) are in catalina's  server.xml:

<Server port="8005" shutdown="SHUTDOWN">
  <Listener className="org.apache.catalina.startup.VersionLoggerListener"/>
  <Listener SSLEngine="on" className="org.apache.catalina.core.AprLifecycleListener"/>
  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener"/>
  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener"/>
  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener"/>
  <GlobalNamingResources>
    <Resource auth="Container"
              description="User database that can be updated and saved"
              factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
              name="UserDatabase"
              pathname="conf/tomcat-users.xml"
              type="org.apache.catalina.UserDatabase"/>
  </GlobalNamingResources>
  <Service name="Catalina">
    <Connector connectionTimeout="20000"
               port="8080"
               protocol="HTTP/1.1"
               redirectPort="8443"/>
    <Engine defaultHost="localhost" name="Catalina">
      <Realm className="org.apache.catalina.realm.LockOutRealm">
        <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
               resourceName="UserDatabase"/>
      </Realm>
      <Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true">
        <Valve className="org.apache.catalina.valves.AccessLogValve"
               directory="logs"
               pattern="%h %l %u %t &quot;%r&quot; %s %b"
               prefix="localhost_access_log"
               suffix=".txt"/>
        <Context docBase="../../../../../wow/src/main/webapp"
                 path="/wow"
                 source="org.apache.catalina.core.StandardContext" />
        <Context docBase="wow"
                 path="/wow/servlet"
                 reloadable="true"
                 source="org.eclipse.jst.jee.server:wow"/>
      </Host>
    </Engine>
  </Service>
</Server>

Then, put the static image contents into the wow/src/main/webapp/images directory, and CSS contents under wow/src/main/webapp/css.
