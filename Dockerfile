FROM  openjdk:24-ea-16-jdk-slim-bullseye
COPY target/SecuredWebEntryPoint-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]