# Building the App with Maven
FROM maven:3.6.1-jdk-8-slim

RUN mkdir /app
WORKDIR /app

# Run Maven build
COPY pom.xml pom.xml
RUN mvn dependency:go-offline

COPY src src
RUN mvn package

FROM tomcat:8

RUN rm -rf /usr/local/tomcat/webapps/ROOT/*

COPY --from=0 /app/target/customer/ /usr/local/tomcat/webapps/ROOT