FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el ejecutable generado localmente
COPY target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]