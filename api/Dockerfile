# 1) Compilar con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# 2) Imagen final, solo con Java y el .jar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S banquillo && adduser -S banquillo -G banquillo
COPY --from=build /app/target/api-*.jar app.jar
USER banquillo
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
