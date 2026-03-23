FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

#RUN mvn dependency:go-ofline -B

COPY src ./src

RUN mvn clean package

#FROM maven:3.8.7-openjdk-18-slim AS runner
FROM openjdk:26-ea-21-jdk-oraclelinux8 AS runner

WORKDIR /app

COPY --from=builder ./app/target/billing-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]
