FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar ejecutable generado
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]