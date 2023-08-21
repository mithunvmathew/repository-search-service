FROM eclipse-temurin:17-jdk-alpine
MAINTAINER mithun
COPY ./build/libs/repository-search-service-0.0.1-SNAPSHOT.jar repository-search-server-0.0.1.jar
RUN sh -c 'touch repository-search-server-0.0.1.jar'
ENTRYPOINT ["java","-jar","/repository-search-server-0.0.1.jar"]