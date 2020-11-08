FROM maven:3.6.0-jdk-8-slim AS build
ENV APP=/home/app
RUN mkdir -p $APP
WORKDIR $APP
COPY pom.xml $APP
RUN mvn dependency:go-offline -B
RUN mvn -T 1C install && rm -rf target