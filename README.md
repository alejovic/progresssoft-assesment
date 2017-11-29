# progresssoft-assessment

Hi there,

This is a small development about how insert massive data in a Database (MySQL) using diferent strategies (Hibernate/JDBC, NativeQuery, Multirow). However the best choice was parallelism with "Executors" and JPA.

Technical Specs :

- Access to DB should be through JPA. -> OK
- For DB type, you can select between (MySql or MongoDB) -> MySQL
- Provide a web interface for uploading files and inquire about results "using filename" following web applications 3 tier architecture. Spring Batch is not allowed. - JPA / EJB / JSF
- Provide a web interface for inquire about results "using filename" .· -> OK
- We expect you to generate sample data set to use it during development  -> OK
- We expect the system to display a summary report for each file after process is finished; summary may contain process duration, number of imported deals and number of invalid records.  -> OK
- We expect you to generate sample data set to use it during development.  -> OK

Deliverables:

- Workable deployment including sample file.
- Deployment steps including sample data of 100K records, contains all invalid/valid records that the system handles.
- Maven or Gradle project is required for full source code.
- Proper error/exception handling.
- Proper Logging.

Frameworks, Api's, Libraries : Junit, Mockito, Hibernate

Some Helpers Source Code in order to develop this assesment:

How to setup a JDBC connection in Glassfish
https://computingat40s.wordpress.com/how-to-setup-a-jdbc-connection-in-glassfish/

The best way to do batch processing with JPA and Hibernate
https://vladmihalcea.com/2017/04/25/the-best-way-to-do-batch-processing-with-jpa-and-hibernate/

Interface ExecutorService
https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html

https://javaee.github.io/glassfish/download

Hibernate 5 + Glassfish 4.1.1: java.lang.NoSuchMethodError: org.jboss.logging.Logger.debugf(Ljava/lang/String;I)V
https://stackoverflow.com/questions/34813782/hibernate-5-glassfish-4-1-1-java-lang-nosuchmethoderror-org-jboss-logging-lo

Alejandro Victoria.
