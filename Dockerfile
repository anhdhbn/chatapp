#
# Build stage
#
FROM anhdhbn/np-server-lib:latest AS build
COPY src $APP/src
RUN mvn clean package -DskipTests
#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/np-chatapp-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/np-server.jar
EXPOSE 1699
EXPOSE 1700
CMD ["java","-cp","/usr/local/lib/np-server.jar", "npserver.MainServer"]