# CSVApplication

Steps to Setup
1. Clone the repository

git clone https://github.com/ihcuk/CSVApplication.git
2. Specify the file uploads directory

Open src/main/resources/application.properties file and change the property upload-dir to the path where you want the error file to be stored.
upload-dir=C:/Users/Public/

Also we can the database specific properties :

spring.datasource.url=jdbc:mysql://localhost:3306/assignment
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#
# Show or not log for each sql query
spring.jpa.show-sql = true
#
# Naming strategy
#
spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
#
# Allows Hibernate to generate SQL optimized for a particular DBMS
#
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

spring.jpa.hibernate.current_session_context_class=thread
spring.jpa.hibernate.ddl-auto=create

2. Run the app using maven

cd CSVApplication
mvn spring-boot:run
The application can be accessed at http://localhost:8080.

You may also package the application in the form of a jar and then run the jar file like so -

mvn clean package
java -jar target/CSVApplication-0.0.1-SNAPSHOT.jar