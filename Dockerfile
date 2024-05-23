FROM maven:3.9-sapmachine-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests




FROM openjdk:21-jdk-oracle

COPY --from=build /app/target/spring_vivacventures_docker.jar /spring_vivacventures_docker.jar
ENTRYPOINT ["java","-jar","/spring_vivacventures_docker.jar"]
