# Bacheca
Application for managing published documents inside VVF offices (ODG/ODS).

The application is the result of a university project for SOSE (Service-oriented Software Engineering) course at University of L'Aquila.

The application is made of 3 differnt projects (Bacheca, AuthServiceSOAP-VVF and MailServiceSOAP-VVF) where Bacheca is the main one and 
will use the others as support services (namely AAA - "Authentication, Authorization and Accouting" and mail delivery).

You will need postgres database (we provived a restore SQL file with some data) and SQL schema creation (empty).
You also need Tomcat 8.5+ and Java 1.8 with PGSQL JDBC driver (latest version). 

The webservice provider used is Apache CXF.

To confiure Tomcat simply put the jdbc driver file (JAR) into _$TOMCAT_HOME\lib_ directory then open under _$TOMCAT_HOME\conf_ the _context.xml_ file and put anywhere under the `<Context>...</Context>` tags the following:

```
<Resource name="jdbc/bacheca" auth="Container"
          type="javax.sql.DataSource" driverClassName="org.postgresql.Driver"
          url="jdbc:postgresql://localhost/postgres?currentSchema=bacheca"
          username="postgres" password="YOURPASSWORD" maxTotal="10" maxIdle="5"
		  maxWaitMillis="-1"/>
```

Please, put the correct username/password for your database and adjust any database/schema reference in the url property.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
