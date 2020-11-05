#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/np-chatapp-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/np-server.jar
EXPOSE 1699
CMD ["java","-cp","/usr/local/lib/np-server.jar", "npserver.MainServer"]