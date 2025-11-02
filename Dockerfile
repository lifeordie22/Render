FROM amazoncorretto:21-alpine-jdk

COPY target/juegosunpaapirest-0.0.1-SNAPSHOT.jar api.jar

ENTRYPOINT ["java","-jar","/api.jar"]